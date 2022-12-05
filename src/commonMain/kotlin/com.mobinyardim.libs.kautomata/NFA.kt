package com.mobinyardim.libs.kautomata

class NFA<T : Enum<T>>(
    startState: State = State(0, "s0", false)
) : Automata<T>(startState) {
    fun addEdge(startState: State, transition: T?, endState: State) {
        _addEdge(startState, transition, endState)
    }
}