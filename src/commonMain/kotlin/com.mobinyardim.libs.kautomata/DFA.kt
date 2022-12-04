package com.mobinyardim.libs.kautomata

class DFA<T : Enum<T>>(
    startState: State = State(0, "s0", false)
) : Automata<T>(startState) {

    fun addEdge(startState: State, transition: T, endState: State) {
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