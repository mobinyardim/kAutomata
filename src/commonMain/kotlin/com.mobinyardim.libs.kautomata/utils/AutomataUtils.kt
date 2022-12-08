package com.mobinyardim.libs.kautomata.utils

import com.mobinyardim.libs.kautomata.State

fun <T : Enum<T>> removeEdge(
    edges: MutableMap<State, MutableMap<State, Set<T?>>>,
    startState: State,
    transition: T?,
    endState: State
) {
    if (edges[startState]?.get(endState) != null &&
        edges[startState]?.get(endState)?.contains(transition) == true
    ) {
        val startToEndTransitions = edges[startState]!![endState] ?: setOf()
        edges[startState]!![endState] = startToEndTransitions.filter {
            it != transition
        }.toSet()
    }
}