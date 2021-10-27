package com.noah.scorereporter.pages

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.asLiveData
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.noah.scorereporter.MainCoroutineRule
import com.noah.scorereporter.TestConstants
import com.noah.scorereporter.data.local.GameDao
import com.noah.scorereporter.data.local.SeasonDao
import com.noah.scorereporter.data.local.TeamDao
import com.noah.scorereporter.data.local.UserDao
import com.noah.scorereporter.data.model.Role
import com.noah.scorereporter.data.network.PageDataSource
import com.noah.scorereporter.data.network.PageNetworkError
import com.noah.scorereporter.data.network.UserDataSource
import com.noah.scorereporter.fake.FakeAndroidKeyStore
import com.noah.scorereporter.fake.FakePageDataSource
import com.noah.scorereporter.fake.FakeUserDataSource
import com.noah.scorereporter.getOrAwaitValue
import com.noah.scorereporter.data.model.Follower
import com.noah.scorereporter.data.model.TeamFollower
import com.noah.scorereporter.util.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import java.util.concurrent.TimeoutException

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class PageRepositoryTest {

    private lateinit var repository: IPageRepository
    private lateinit var pageDataSource: PageDataSource
    private lateinit var userDataSource: UserDataSource
    private lateinit var teamDao: TeamDao
    private lateinit var seasonDao: SeasonDao
    private lateinit var userDao: UserDao
    private lateinit var gameDao: GameDao
    private lateinit var context: Context
    private lateinit var masterKey: MasterKey
    private lateinit var sharedPrefs: SharedPreferences

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        pageDataSource = FakePageDataSource()
        userDataSource = FakeUserDataSource()
        teamDao = mock(TeamDao::class.java)
        seasonDao = mock(SeasonDao::class.java)
        userDao = mock(UserDao::class.java)
        gameDao = mock(GameDao::class.java)

        context = ApplicationProvider.getApplicationContext()

        masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        sharedPrefs = EncryptedSharedPreferences.create(
            context,
            Constants.SHARED_PREFS,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        `when`(teamDao.getTeamById(TestConstants.TEAM_RESPONSE_1.id)).thenReturn(
            flow { emit(TestConstants.TEAM_RESPONSE_1) }
        )
        `when`(teamDao.getTeamById(TestConstants.TEAM_RESPONSE_2.id)).thenReturn(
            flow { emit(TestConstants.TEAM_RESPONSE_2) }
        )
        `when`(teamDao.getTeamById("id1")).thenReturn(flow { })
        `when`(teamDao.hasTeam(TestConstants.TEAM_RESPONSE_1.id)).thenReturn(true)
        `when`(teamDao.hasTeam(TestConstants.TEAM_RESPONSE_2.id)).thenReturn(true)

        `when`(seasonDao.getSeasonById(TestConstants.SEASON_RESPONSE.id)).thenReturn(
            flow { emit (TestConstants.SEASON_RESPONSE) }
        )
        `when`(seasonDao.getSeasonById(TestConstants.SEASON_RESPONSE_2.id)).thenReturn(
            flow { emit (TestConstants.SEASON_RESPONSE_2) }
        )
        `when`(seasonDao.hasSeason(TestConstants.SEASON_RESPONSE.id)).thenReturn(true)
        `when`(seasonDao.hasSeason(TestConstants.SEASON_RESPONSE_2.id)).thenReturn(true)

        `when`(userDao.getUserById(TestConstants.USER_PROFILE_1.id)).thenReturn(
            flow { emit(TestConstants.USER_PROFILE_1) }
        )
        `when`(userDao.getUserById(TestConstants.USER_PROFILE_2.id)).thenReturn(
            flow { emit(TestConstants.USER_PROFILE_2) }
        )
        `when`(userDao.hasUser(TestConstants.USER_PROFILE_1.id)).thenReturn(true)
        `when`(userDao.hasUser(TestConstants.USER_PROFILE_2.id)).thenReturn(true)

        `when`(gameDao.hasGame(TestConstants.GAME_1.id)).thenReturn(true)
        `when`(gameDao.hasGame(TestConstants.GAME_2.id)).thenReturn(true)

        `when`(gameDao.getGameById(TestConstants.GAME_1.id)).thenReturn(
            flow { emit(TestConstants.GAME_1) }
        )
        `when`(gameDao.getGameById(TestConstants.GAME_2.id)).thenReturn(
            flow { emit(TestConstants.GAME_2) }
        )

        repository = PageRepository(
            pageDataSource,
            userDataSource,
            sharedPrefs,
            teamDao,
            seasonDao,
            userDao,
            gameDao
        )
    }

    @Test
    fun `test getTeamById with existing user`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true
        val result = repository.getTeamById(TestConstants.TEAM_RESPONSE_1.id).asLiveData()
        verify(teamDao).hasTeam(TestConstants.TEAM_RESPONSE_1.id)
        verify(teamDao).getTeamById(TestConstants.TEAM_RESPONSE_1.id)
        assertThat(result.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE_1))
    }

    @Test(expected = PageNetworkError::class)
    fun `test getTeamById with user not found`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = false
        repository.getTeamById("id1").asLiveData()
    }

    @Test
    fun `test getTeamById after adding user`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true
        `when`(teamDao.getTeamById("id2")).thenReturn(flow { emit(TestConstants.TEAM_RESPONSE_2) })

        val result = repository.getTeamById("id2").asLiveData()
        verify(teamDao).hasTeam("id2")
        verify(teamDao).getTeamById("id2")

        assertThat(result.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE_2))
    }

    @Test
    fun `test followTeam with existing user`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true

        val result = repository.followTeam(TestConstants.TEAM_RESPONSE_1.id).asLiveData()
        verify(teamDao).getTeamById(TestConstants.TEAM_RESPONSE_1.id)

        assertThat(result.getOrAwaitValue(), `is`(TestConstants.TEAM_RESPONSE_1))
    }

    @Test(expected = PageNetworkError::class)
    fun `test followTeam with non existent user`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = false
        repository.followTeam("id1").asLiveData()
    }

    @Test
    fun `test getSeasonsOfTeam with presaved seasons`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true

        val result = repository.getSeasonsOfTeam(
            listOf(TestConstants.SEASON_RESPONSE.id, TestConstants.SEASON_RESPONSE_2.id)
        ).asLiveData()

        verify(seasonDao, times(2)).hasSeason(TestConstants.SEASON_RESPONSE.id)
        verify(seasonDao, times(2)).hasSeason(TestConstants.SEASON_RESPONSE_2.id)
        verify(seasonDao, times(0)).save(TestConstants.SEASON_RESPONSE, TestConstants.SEASON_RESPONSE_2)
        verify(seasonDao).getSeasonById(TestConstants.SEASON_RESPONSE.id)
        verify(seasonDao).getSeasonById(TestConstants.SEASON_RESPONSE_2.id)

        val list = result.getOrAwaitValue()
        assertThat(list.size, `is`(2))
        assertThat(list[0], `is`(TestConstants.SEASON_RESPONSE))
        assertThat(list[1], `is`(TestConstants.SEASON_RESPONSE_2))
    }

    @Test
    fun `test getSeasonsOfTeam with bad list`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true

        val result = repository.getSeasonsOfTeam(
            listOf("bad_id_1", "bad_id_2")
        ).asLiveData()

        verify(seasonDao, times(2)).hasSeason("bad_id_1")
        verify(seasonDao, times(2)).hasSeason("bad_id_2")
        verify(seasonDao, times(0)).getSeasonById("bad_id_1")
        verify(seasonDao, times(0)).getSeasonById("bad_id_2")

        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test getSeasonsOfTeam with invalid source`() = mainCoroutineRule.runBlockingTest {
        `when`(seasonDao.hasSeason(TestConstants.SEASON_RESPONSE.id)).thenReturn(false)
        `when`(seasonDao.hasSeason(TestConstants.SEASON_RESPONSE_2.id)).thenReturn(false)
        (pageDataSource as FakePageDataSource).valid = false

        val result = repository.getSeasonsOfTeam(
            listOf(TestConstants.SEASON_RESPONSE.id, TestConstants.SEASON_RESPONSE_2.id)
        ).asLiveData()

        verify(seasonDao, times(2)).hasSeason(TestConstants.SEASON_RESPONSE.id)
        verify(seasonDao, times(2)).hasSeason(TestConstants.SEASON_RESPONSE_2.id)
        verify(seasonDao, times(0)).getSeasonById(TestConstants.SEASON_RESPONSE.id)
        verify(seasonDao, times(0)).getSeasonById(TestConstants.SEASON_RESPONSE_2.id)

        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test getFollowersOfTeam with presaved seasons`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true

        val result = repository.getFollowersOfTeam(
            listOf(
                TeamFollower(TestConstants.USER_PROFILE_1.id, Role.COACH),
                TeamFollower(TestConstants.USER_PROFILE_2.id, Role.PLAYER)
            )
        ).asLiveData()

        verify(userDao, times(2)).hasUser(TestConstants.USER_PROFILE_1.id)
        verify(userDao, times(2)).hasUser(TestConstants.USER_PROFILE_2.id)
        verify(userDao).getUserById(TestConstants.USER_PROFILE_1.id)
        verify(userDao).getUserById(TestConstants.USER_PROFILE_2.id)

        val list = result.getOrAwaitValue()
        assertThat(list.size, `is`(2))
        assertThat(list[0], `is`(
            Follower(
            TestConstants.USER_PROFILE_1.firstName,
            TestConstants.USER_PROFILE_1.lastName,
            TestConstants.USER_PROFILE_1.email,
            Role.COACH
        )
        ))
        assertThat(list[1], `is`(
            Follower(
            TestConstants.USER_PROFILE_2.firstName,
            TestConstants.USER_PROFILE_2.lastName,
            TestConstants.USER_PROFILE_2.email,
            Role.PLAYER
        )
        ))
    }

    @Test
    fun `test getFollowersOfTeam with invalid list`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true

        val result = repository.getFollowersOfTeam(
            listOf(
                TeamFollower("bad_id_1", Role.COACH),
                TeamFollower("bad_id_2", Role.PLAYER)
            )
        ).asLiveData()

        verify(userDao, times(2)).hasUser("bad_id_1")
        verify(userDao, times(2)).hasUser("bad_id_2")
        verify(userDao, times(0)).getUserById("bad_id_1")
        verify(userDao, times(0)).getUserById("bad_id_2")

        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test getFollowersOfTeam with invalid source`() = mainCoroutineRule.runBlockingTest {
        `when`(userDao.hasUser(TestConstants.USER_PROFILE_1.id)).thenReturn(false)
        `when`(userDao.hasUser(TestConstants.USER_PROFILE_2.id)).thenReturn(false)
        (pageDataSource as FakePageDataSource).valid = false

        val result = repository.getFollowersOfTeam(
            listOf(
                TeamFollower(TestConstants.USER_PROFILE_1.id, Role.COACH),
                TeamFollower(TestConstants.USER_PROFILE_2.id, Role.PLAYER)
            )
        ).asLiveData()

        verify(userDao, times(2)).hasUser(TestConstants.USER_PROFILE_1.id)
        verify(userDao, times(2)).hasUser(TestConstants.USER_PROFILE_2.id)
        verify(userDao, times(0)).getUserById(TestConstants.USER_PROFILE_1.id)
        verify(userDao, times(0)).getUserById(TestConstants.USER_PROFILE_2.id)

        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test canFollow without valid jwt`() = mainCoroutineRule.runBlockingTest {
        (userDataSource as FakeUserDataSource).shouldReturnError = false
        val result = repository.canFollow(TestConstants.TEAM_RESPONSE_1.id)
        assertThat(result, `is`(false))
    }

    @Test
    fun `test canFollow with valid jwt and team`() = mainCoroutineRule.runBlockingTest {
        (userDataSource as FakeUserDataSource).shouldReturnError = false
        sharedPrefs.edit().putString(Constants.USER_TOKEN, "jwt").commit()
        val result = repository.canFollow(TestConstants.TEAM_RESPONSE_1.id)
        assertThat(result, `is`(false))
    }

    @Test
    fun `test canFollow with invalid user`() = mainCoroutineRule.runBlockingTest {
        (userDataSource as FakeUserDataSource).shouldReturnError = true
        sharedPrefs.edit().putString(Constants.USER_TOKEN, "jwt").commit()
        val result = repository.canFollow(TestConstants.TEAM_RESPONSE_1.id)
        assertThat(result, `is`(false))
    }

    @Test
    fun `test canFollow with trueCase`() = mainCoroutineRule.runBlockingTest {
        (userDataSource as FakeUserDataSource).shouldReturnError = false
        sharedPrefs.edit().putString(Constants.USER_TOKEN, "jwt").commit()
        val result = repository.canFollow("bad_team")
        assertThat(result, `is`(true))
    }

    @Test
    fun `test getSeasonById with season in room`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true
        val result = repository.getSeasonById(TestConstants.SEASON_RESPONSE.id).asLiveData()
        verify(seasonDao).hasSeason(TestConstants.SEASON_RESPONSE.id)
        verify(seasonDao, times(0)).save(TestConstants.SEASON_RESPONSE)
        verify(seasonDao).getSeasonById(TestConstants.SEASON_RESPONSE.id)
        assertThat(result.getOrAwaitValue(), `is`(TestConstants.SEASON_RESPONSE))
    }

    @Test
    fun `test getSeasonById with season in backend`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true
        `when`(seasonDao.hasSeason(TestConstants.SEASON_RESPONSE_2.id)).thenReturn(false)
        val result = repository.getSeasonById(TestConstants.SEASON_RESPONSE_2.id).asLiveData()
        verify(seasonDao).hasSeason(TestConstants.SEASON_RESPONSE_2.id)
        // verify(seasonDao).save(TestConstants.SEASON_RESPONSE_2)
        verify(seasonDao).getSeasonById(TestConstants.SEASON_RESPONSE_2.id)

        assertThat(result.getOrAwaitValue(), `is`(TestConstants.SEASON_RESPONSE_2))
    }

    @Test(expected = PageNetworkError::class)
    fun `test getSeasonById with nonexistent season`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true
        val result = repository.getSeasonById("badid1")
    }

    @Test(expected = PageNetworkError::class)
    fun `test getSeasonById with invalid source`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = false
        val result = repository.getSeasonById("badid1")
    }

    @Test
    fun `test getGamesOfSeason with presaved games`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true
        val result = repository.getGamesOfSeason(listOf(TestConstants.GAME_1.id, TestConstants.GAME_2.id)).asLiveData()
        verify(gameDao).hasGame(TestConstants.GAME_1.id)
        verify(gameDao).hasGame(TestConstants.GAME_2.id)
        verify(gameDao).getGameById(TestConstants.GAME_1.id)
        verify(gameDao).getGameById(TestConstants.GAME_2.id)

        val list = result.getOrAwaitValue()
        assertThat(list.size, `is`(2))
        assertThat(list, `is`(listOf(TestConstants.GAME_1, TestConstants.GAME_2)))
    }

    @Test
    fun `test getGamesOfSeason with valid data source`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true
        `when`(gameDao.hasGame(TestConstants.GAME_1.id)).thenReturn(false)
        `when`(gameDao.hasGame(TestConstants.GAME_2.id)).thenReturn(false)
        val result = repository.getGamesOfSeason(listOf(TestConstants.GAME_1.id, TestConstants.GAME_2.id)).asLiveData()
        verify(gameDao).hasGame(TestConstants.GAME_1.id)
        verify(gameDao).hasGame(TestConstants.GAME_2.id)
        verify(gameDao).getGameById(TestConstants.GAME_1.id)
        verify(gameDao).getGameById(TestConstants.GAME_2.id)

        val list = result.getOrAwaitValue()
        assertThat(list.size, `is`(2))
        assertThat(list, `is`(listOf(TestConstants.GAME_1, TestConstants.GAME_2)))
    }

    @Test
    fun `test getGamesOfSeason with invalid data source`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = false
        `when`(gameDao.hasGame(TestConstants.GAME_1.id)).thenReturn(false)
        `when`(gameDao.hasGame(TestConstants.GAME_2.id)).thenReturn(false)

        val result = repository.getGamesOfSeason(listOf(TestConstants.GAME_1.id, TestConstants.GAME_2.id)).asLiveData()
        verify(gameDao).hasGame(TestConstants.GAME_1.id)
        verify(gameDao).hasGame(TestConstants.GAME_2.id)
        verify(gameDao).getGameById(TestConstants.GAME_1.id)
        verify(gameDao).getGameById(TestConstants.GAME_2.id)

        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test getGameListItems with valid data source`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true

        val result = repository.getGameListItems(listOf(TestConstants.GAME_1, TestConstants.GAME_2)).asLiveData()
        verify(teamDao, times(4)).hasTeam(TestConstants.TEAM_RESPONSE_1.id)
        verify(teamDao, times(4)).hasTeam(TestConstants.TEAM_RESPONSE_2.id)
        verify(teamDao, times(2)).getTeamById(TestConstants.TEAM_RESPONSE_1.id)
        verify(teamDao, times(2)).getTeamById(TestConstants.TEAM_RESPONSE_2.id)

        val list = result.getOrAwaitValue()
        assertThat(list.size, `is`(2))
        assertThat(list[0], `is`(TestConstants.GAME_LIST_ITEM_1))
        assertThat(list[1], `is`(TestConstants.GAME_LIST_ITEM_2))
    }

    @Test
    fun `test getGameListItems without saved games`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true
        `when`(teamDao.hasTeam(TestConstants.GAME_1.id)).thenReturn(false)
        `when`(teamDao.hasTeam(TestConstants.GAME_1.id)).thenReturn(false)

        val result = repository.getGameListItems(listOf(TestConstants.GAME_1, TestConstants.GAME_2)).asLiveData()
        verify(teamDao, times(4)).hasTeam(TestConstants.TEAM_RESPONSE_1.id)
        verify(teamDao, times(4)).hasTeam(TestConstants.TEAM_RESPONSE_2.id)
        verify(teamDao, times(2)).getTeamById(TestConstants.TEAM_RESPONSE_1.id)
        verify(teamDao, times(2)).getTeamById(TestConstants.TEAM_RESPONSE_2.id)

        val list = result.getOrAwaitValue()
        assertThat(list.size, `is`(2))
        assertThat(list[0], `is`(TestConstants.GAME_LIST_ITEM_1))
        assertThat(list[1], `is`(TestConstants.GAME_LIST_ITEM_2))
    }

    @Test
    fun `test getGamesListItems with invalid data source`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = false

        `when`(teamDao.hasTeam(TestConstants.GAME_1.id)).thenReturn(false)
        `when`(teamDao.hasTeam(TestConstants.GAME_1.id)).thenReturn(false)

        val result = repository.getGameListItems(listOf(TestConstants.GAME_1, TestConstants.GAME_2)).asLiveData()
        verify(teamDao, times(4)).hasTeam(TestConstants.TEAM_RESPONSE_1.id)
        verify(teamDao, times(4)).hasTeam(TestConstants.TEAM_RESPONSE_2.id)
        verify(teamDao, times(2)).getTeamById(TestConstants.TEAM_RESPONSE_1.id)
        verify(teamDao, times(2)).getTeamById(TestConstants.TEAM_RESPONSE_2.id)

        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    @Test
    fun `test getGame with valid source`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true

        `when`(gameDao.hasGame(TestConstants.GAME_1.id)).thenReturn(true)
        `when`(teamDao.hasTeam(TestConstants.GAME_1.awayTeam)).thenReturn(true)
        `when`(teamDao.hasTeam(TestConstants.GAME_1.homeTeam)).thenReturn(true)
        `when`(seasonDao.hasSeason(TestConstants.GAME_1.season)).thenReturn(true)

        val result = repository.getGame(TestConstants.GAME_1.id).asLiveData()
        verify(gameDao, times(1)).hasGame(TestConstants.GAME_1.id)
        verify(teamDao, times(1)).hasTeam(TestConstants.GAME_1.awayTeam)
        verify(teamDao, times(1)).hasTeam(TestConstants.GAME_1.homeTeam)
        verify(seasonDao, times(1)).hasSeason(TestConstants.GAME_1.season)

        verify(gameDao).getGameById(TestConstants.GAME_1.id)
        verify(teamDao).getTeamById(TestConstants.GAME_1.awayTeam)
        verify(teamDao).getTeamById(TestConstants.GAME_1.homeTeam)
        verify(seasonDao).getSeasonById(TestConstants.GAME_1.season)

        val game = result.getOrAwaitValue()
        assertThat(game, `is`(TestConstants.GAME_ITEM_1))
    }

    @Test
    fun `test getGame without saved members`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = true

        `when`(gameDao.hasGame(TestConstants.GAME_1.id)).thenReturn(false)
        `when`(teamDao.hasTeam(TestConstants.GAME_1.awayTeam)).thenReturn(false)
        `when`(teamDao.hasTeam(TestConstants.GAME_1.homeTeam)).thenReturn(false)
        `when`(seasonDao.hasSeason(TestConstants.GAME_1.season)).thenReturn(false)

        val result = repository.getGame(TestConstants.GAME_1.id).asLiveData()
        verify(gameDao, times(1)).hasGame(TestConstants.GAME_1.id)
        verify(teamDao, times(1)).hasTeam(TestConstants.GAME_1.awayTeam)
        verify(teamDao, times(1)).hasTeam(TestConstants.GAME_1.homeTeam)
        verify(seasonDao, times(1)).hasSeason(TestConstants.GAME_1.season)

        verify(gameDao).getGameById(TestConstants.GAME_1.id)
        verify(teamDao).getTeamById(TestConstants.GAME_1.awayTeam)
        verify(teamDao).getTeamById(TestConstants.GAME_1.homeTeam)
        verify(seasonDao).getSeasonById(TestConstants.GAME_1.season)

        val game = result.getOrAwaitValue()
        assertThat(game, `is`(TestConstants.GAME_ITEM_1))
    }

    @Test
    fun `test getGame with invalid data source`() = mainCoroutineRule.runBlockingTest {
        (pageDataSource as FakePageDataSource).valid = false

        `when`(gameDao.hasGame(TestConstants.GAME_1.id)).thenReturn(false)
        `when`(teamDao.hasTeam(TestConstants.GAME_1.awayTeam)).thenReturn(false)
        `when`(teamDao.hasTeam(TestConstants.GAME_1.homeTeam)).thenReturn(false)
        `when`(seasonDao.hasSeason(TestConstants.GAME_1.season)).thenReturn(false)

        val result = repository.getGame(TestConstants.GAME_1.id).asLiveData()
        verify(gameDao, times(1)).hasGame(TestConstants.GAME_1.id)
        verify(teamDao, times(1)).hasTeam(TestConstants.GAME_1.awayTeam)
        verify(teamDao, times(1)).hasTeam(TestConstants.GAME_1.homeTeam)
        verify(seasonDao, times(1)).hasSeason(TestConstants.GAME_1.season)

        verify(gameDao, times(0)).getGameById(TestConstants.GAME_1.id)
        verify(teamDao, times(0)).getTeamById(TestConstants.GAME_1.awayTeam)
        verify(teamDao, times(0)).getTeamById(TestConstants.GAME_1.homeTeam)
        verify(seasonDao, times(0)).getSeasonById(TestConstants.GAME_1.season)


        try {
            result.getOrAwaitValue()
        } catch (exception: TimeoutException) {
            assertThat(exception.message, `is`(TestConstants.LIVE_DATA_ERROR))
        }
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            FakeAndroidKeyStore.setup
        }
    }
}