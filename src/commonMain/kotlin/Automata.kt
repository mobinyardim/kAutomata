import exceptions.DuplicatedStateException
import exceptions.NoSuchStateException

abstract class Automata<T : Enum<T>> {

    private val states: MutableSet<State> = mutableSetOf()
    fun addState(state: State): State {
        if (states.contains(state)) {
            throw DuplicatedStateException(state.name)
        }
        states.add(state)
        return state
    }

    private val edges: MutableMap<State, Map<T, Set<State>>> = mutableMapOf()
    open fun addEdge(startState: State, transition: T, endState: State) {
        if (!states.contains(startState) || !states.contains(endState))
            throw NoSuchStateException()

        val stateEdges = edges[startState]?.toMutableMap() ?: mutableMapOf()
        val newEdges = stateEdges[transition]?.toMutableSet() ?: mutableSetOf()

        //If already there is a transition then return
        if (newEdges.contains(endState)) {
            return
        }
        newEdges.add(endState)
        stateEdges[transition] = newEdges

        if (edges[startState]?.contains(transition) == true) {
            edges.remove(startState)
            edges[startState] = stateEdges
        }else{
            edges[startState] = stateEdges
        }
    }
}



