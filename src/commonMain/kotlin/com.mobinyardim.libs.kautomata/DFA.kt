package com.mobinyardim.libs.kautomata

import com.mobinyardim.libs.kautomata.edge.Edge
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException

class DFA<T : Enum<T>>(
    startState: State = State(0, "s0", false)
) : Automata<T>(startState) {

    fun addEdge(startState: State, transition: T, endState: State) {
        if (edges.contain(Edge(start = startState, transition = transition, end = endState))) {
            throw DuplicatedEdgeException(
                startState, transition.name, endState
            )
        }

        removeEdge(
            startState = startState,
            transition = transition
        )
        _addEdge(
            startState = startState,
            transition = transition,
            endState = endState
        )
    }
}