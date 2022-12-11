package com.mobinyardim.libs.kautomata

import com.mobinyardim.libs.kautomata.edge.Edge

class NFA<T : Enum<T>>(
    startState: State = State(0, "s0", false)
) : Automata<T>(startState) {
    fun addEdge(edge: Edge<T>) {
        _addEdge(edge)
    }
}