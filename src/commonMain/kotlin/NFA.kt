import exceptions.DuplicatedEdgeException
import exceptions.NoSuchStateException

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

        val stateEdges = edges[startState]?.toMutableMap() ?: mutableMapOf()
        val newEdges = stateEdges[transition]?.toMutableSet() ?: mutableSetOf()



        newEdges.add(endState)
        stateEdges[transition] = newEdges

        if (edges[startState]?.contains(transition) == true) {
            edges.remove(startState)
            edges[startState] = stateEdges
        } else {
            edges[startState] = stateEdges
        }
    }
}