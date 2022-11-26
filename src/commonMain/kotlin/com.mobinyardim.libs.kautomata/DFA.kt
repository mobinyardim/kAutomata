package com.mobinyardim.libs.kautomata

class DFA<T : Enum<T>>(
    startState: State = State(0, "s0", false)
) : Automata<T>(startState) {

    fun addEdge(startState: State, transition: T, endState: State) {
        if (!containsState(startState) || !containsState(endState))
            throw NoSuchStateException()

        if (containsEdge(startState, transition, endState)) {
            throw DuplicatedEdgeException(
                startState, transition.name, endState
            )
        }

        val stateEdges = mutableMapOf<T, State>()

        stateEdges[transition] = endState

        if (_edges[startState]?.contains(transition) == true) {
            _edges.remove(startState)
            _edges[startState] = stateEdges.mapValues {
                setOf(it.value)
            }
        } else {
            _edges[startState] = stateEdges.mapValues {
                setOf(it.value)
            }
        }
    }
}