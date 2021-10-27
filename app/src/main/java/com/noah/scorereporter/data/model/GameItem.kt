package com.noah.scorereporter.data.model

import java.util.*

data class GameItem(
    val awayTeam: Team,
    val homeTeam: Team,
    val season: Season,
    val innings: GameInnings,
    val winner: TeamStatus,
    val date: Date
)
