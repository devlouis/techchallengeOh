package com.example.apptechchallengeoh.home.domain.model.Intervalscheduling

import com.example.apptechchallengeoh.home.domain.model.Event

data class IntervalEvent(
    val event: Event,
    val start: Long,
    val end: Long,
    val weight: Int  // mapeo de prioridad
)