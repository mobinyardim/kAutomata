import exceptions.DuplicatedEdgeException
import exceptions.DuplicatedStateException
import exceptions.NoSuchStateException

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

    private val edges: MutableMap<State, Map<T, Set<State>>> = mutableMapOf()
    open fun addEdge(startState: State, transition: T, endState: State) {
        if (!states.contains(startState) || !states.contains(endState))
            throw NoSuchStateException()

        val stateEdges = edges[startState]?.toMutableMap() ?: mutableMapOf()
        val newEdges = stateEdges[transition]?.toMutableSet() ?: mutableSetOf()

        //If already there is a transition then return
        if (newEdges.contains(endState)) {
            throw DuplicatedEdgeException(
                startState, transition.name, endState
            )
        }

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



