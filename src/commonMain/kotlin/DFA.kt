import exceptions.DuplicatedEdgeException
import exceptions.NoSuchStateException

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

        if (edges[startState]?.contains(transition) == true) {
            edges.remove(startState)
            edges[startState] = stateEdges.mapValues {
                setOf(it.value)
            }
        } else {
            edges[startState] = stateEdges.mapValues {
                setOf(it.value)
            }
        }
    }
}