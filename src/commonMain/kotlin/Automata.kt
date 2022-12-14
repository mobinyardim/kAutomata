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

    protected open val edges: MutableMap<State, Edge<T?>> = mutableMapOf()

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
            val nextStatesWithLambda = edges[currentState]?.next(null)
            if (nextStates.isNullOrEmpty()) {
                automataStateTracer.onTrap(currentState, string)
            } else {
                nextStates.forEach {
                    automataStateTracer.onTransition(currentState, firstCharacter, it)
                    trace(
                        string = string.subList(1, string.size),
                        automataStateTracer = automataStateTracer,
                        currentState = it
                    )
                }
            }
            if (!nextStatesWithLambda.isNullOrEmpty()) {
                nextStatesWithLambda.forEach {
                    automataStateTracer.onTransition(currentState, null, it)
                    trace(
                        string = string,
                        automataStateTracer = automataStateTracer,
                        currentState = it
                    )
                }
            }
        }
    }
}



