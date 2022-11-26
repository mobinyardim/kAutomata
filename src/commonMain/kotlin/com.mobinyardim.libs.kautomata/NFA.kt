package com.mobinyardim.libs.kautomata

class NFA<T : Enum<T>>(
    startState: State = State(0, "s0", false)
) : Automata<T>(startState) {
    fun addEdge(startState: State, transition: T?, endState: State) {
        if (!containsState(startState) || !containsState(endState))
            throw NoSuchStateException()

        if (containsEdge(startState, transition, endState)) {
            throw DuplicatedEdgeException(
                startState, transition?.name ?: "lambda", endState
            )
        }

        val stateEdges = _edges[startState]?.toMutableMap() ?: mutableMapOf()
        val newEdges = stateEdges[transition]?.toMutableSet() ?: mutableSetOf()



        newEdges.add(endState)
        stateEdges[transition] = newEdges

        if (_edges[startState]?.contains(transition) == true) {
            _edges.remove(startState)
            _edges[startState] = stateEdges
        } else {
            _edges[startState] = stateEdges
        }
    }
}