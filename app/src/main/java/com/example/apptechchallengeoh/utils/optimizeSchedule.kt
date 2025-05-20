package com.example.apptechchallengeoh.utils

import com.example.apptechchallengeoh.home.domain.model.Event
import com.example.apptechchallengeoh.home.domain.model.Intervalscheduling.IntervalEvent
import com.example.apptechchallengeoh.home.domain.model.Intervalscheduling.priorityWeight
import java.time.Duration

fun optimizeSchedule(events: List<Event>): List<Event> {
    if (events.isEmpty()) return emptyList()

    // Particionar eventos futuros y pasados
    val now = System.currentTimeMillis()
    val intervals = events.map { e ->
        val start = (e.date?.seconds ?: 0L) * 1000L
        val end   = start + Duration.ofHours(1).toMillis()
        IntervalEvent(e, start, end, priorityWeight(e.priority))
    }

    val (futureIntervals, pastIntervals) = intervals.partition { it.end >= now }

    // Optimizar futuros sin solapamientos, maximizando prioridad
    val optimizedUpcoming = optimizeUpcoming(futureIntervals)

    // Ordenar pasados solo por fecha ascendente
    val sortedPast = pastIntervals
        .sortedBy { it.start }
        .map { it.event }

    return optimizedUpcoming + sortedPast
}

private fun optimizeUpcoming(intervals: List<IntervalEvent>): List<Event> {
    if (intervals.isEmpty()) return emptyList()

    // 1) Ordenar por end asc, start asc, peso desc para empates exactos
    val sorted = intervals.sortedWith(
        compareBy<IntervalEvent> { it.end }
            .thenBy { it.start }
            .thenByDescending { it.weight }
    )
    val n = sorted.size

    // 2) Calcular p[j] permitiendo empalme (end <= start)
    val p = IntArray(n) { -1 }
    for (j in 0 until n) {
        var lo = 0
        var hi = j - 1
        var idx = -1
        while (lo <= hi) {
            val mid = (lo + hi) / 2
            if (sorted[mid].end <= sorted[j].start) {
                idx = mid
                lo = mid + 1
            } else {
                hi = mid - 1
            }
        }
        p[j] = idx
    }

    // 3) DP de pesos máximos
    val M = LongArray(n)
    M[0] = sorted[0].weight.toLong()
    for (j in 1 until n) {
        val incl = sorted[j].weight + (if (p[j] >= 0) M[p[j]] else 0L)
        val excl = M[j - 1]
        M[j] = maxOf(incl, excl)
    }

    // 4) Reconstruir conjunto óptimo
    val chosen = mutableSetOf<Int>()
    fun pick(j: Int) {
        if (j < 0) return
        val incl = sorted[j].weight + (if (p[j] >= 0) M[p[j]] else 0L)
        val excl = if (j > 0) M[j - 1] else 0L
        if (incl >= excl) {
            chosen += j
            pick(p[j])
        } else {
            pick(j - 1)
        }
    }
    pick(n - 1)

    // 5) Construir listas: óptimos y resto
    val optimalList = sorted
        .mapIndexed { idx, ie -> ie to idx }
        .filter { (_, idx) -> idx in chosen }
        .sortedBy { it.first.end }
        .map { it.first.event }

    val others = sorted
        .mapIndexed { idx, ie -> ie to idx }
        .filter { (_, idx) -> idx !in chosen }
        .map { it.first.event }
        .sortedWith(
            compareByDescending<Event> { priorityWeight(it.priority) }
                .thenBy { (it.date?.seconds ?: 0L) * 1000L }
        )

    return optimalList + others
}