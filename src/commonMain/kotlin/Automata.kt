import exceptions.DuplicatedEdgeException
import exceptions.DuplicatedStateException
import exceptions.NoSuchStateException

typealias Edge<T> = Map<T, Set<State>>

fun <T> Edge<T>.next(transition: T): Set<State>? {
    return this[transition]
}

abstract class Automata<T : Enum<T>>(private val startState: State = State(0, "s0", false)) {

    private val states: MutableSet<State> = mutableSetOf(startState)
    fun addState(state: State): State {
        if (containsState(state)) {
            throw DuplicatedStateException(state)
        }
        states.add(state)
        return state
    }

    fun getState(stateId: Int): State? {
        return states.firstOrNull { it.id == stateId }
    }

    fun containsState(state: State): Boolean {
        return states.firstOrNull { it.id == state.id } != null
    }

    private val edges: MutableMap<State, Edge<T?>> = mutableMapOf()

    open fun addEdge(startState: State, transition: T?, endState: State) {
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

    fun containsEdge(start: State, transition: T?, endState: State): Boolean {
        return (edges[start] ?: mutableMapOf())[transition]?.any { endState.id == it.id } ?: false
    }

    fun trace(string: List<T>, automataStateTracer: AutomataStateTracer<T>) {
        automataStateTracer.onStart()

        trace(string, automataStateTracer, startState)
    }


    private fun trace(string: List<T>, automataStateTracer: AutomataStateTracer<T>, currentState: State) {
        automataStateTracer.onCurrentStateChange(currentState)

        if (string.isEmpty()) {
            if (currentState.isFinal) {
                automataStateTracer.onFinalState(currentState)
            } else {
                automataStateTracer.onTrap(currentState, string)
            }
        } else {
            val firstCharacter = string.first()
            val nextStates = edges[currentState]?.next(firstCharacter)
            if (nextStates.isNullOrEmpty()) {
                automataStateTracer.onTrap(currentState, string)
            } else {
                nextStates.forEach {
                    trace(
                        string = string.subList(1, string.size),
                        automataStateTracer = automataStateTracer,
                        currentState = it
                    )
                }
            }
        }
    }
}



