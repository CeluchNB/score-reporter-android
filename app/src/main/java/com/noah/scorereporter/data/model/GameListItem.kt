package com.noah.scorereporter.data.model

data class GameListItem(
    val awayTeam: Team,
    val homeTeam: Team,
    val awayScore: Int,
    val homeScore: Int,
    val winner: TeamStatus
)
