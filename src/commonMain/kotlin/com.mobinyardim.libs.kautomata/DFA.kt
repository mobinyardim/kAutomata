package com.mobinyardim.libs.kautomata

import com.mobinyardim.libs.kautomata.edge.Edge
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException

class DFA<T : Enum<T>>(
    startState: State = State(0, "s0", false)
) : Automata<T>(startState) {

    fun addEdge(edge: Edge<T>) {
        if (edges.contain(edge)) {
            throw DuplicatedEdgeException(
                edge
            )
        }

        removeEdge(
            startState = edge.start,
            transition = edge.transition
        )
        _addEdge(edge)
    }
}