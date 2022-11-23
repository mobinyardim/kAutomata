import exceptions.DuplicatedEdgeException
import exceptions.DuplicatedStateException
import exceptions.NoSuchStateException

typealias Edge<T> = Map<T, Set<State>>

fun <T> Edge<T>.next(transition: T): Set<State>? {
    return this[transition]
}

abstract class Automata<T : Enum<T>> {

    private val states: MutableSet<State> = mutableSetOf()
    fun addState(state: State): State {
        if (contains(state)) {
            throw DuplicatedStateException(state)
        }
        states.add(state)
        return state
    }

    fun getState(stateId: Int): State? {
        return states.firstOrNull { it.id == stateId }
    }

    fun contains(state: State): Boolean {
        return states.firstOrNull { it.id == state.id } != null
    }

    private val edges: MutableMap<State, Edge<T>> = mutableMapOf()

    open fun addEdge(startState: State, transition: T, endState: State) {
        if (!contains(startState) || !contains(endState))
            throw NoSuchStateException()

        if (contains(startState, transition, endState)) {
            throw DuplicatedEdgeException(
                startState, transition.name, endState
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

    fun contains(start: State, transition: T, endState: State): Boolean {
        return (edges[start] ?: mutableMapOf())[transition]?.any { endState.id == it.id } ?: false
    }
}



