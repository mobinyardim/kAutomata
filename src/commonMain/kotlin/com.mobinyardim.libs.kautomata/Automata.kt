package com.mobinyardim.libs.kautomata

typealias Edge<T> = Map<T, Set<State>>

fun <T> Edge<T>.next(transition: T): Set<State>? {
    return this[transition]
}

abstract class Automata<T : Enum<T>>(private val startState: State = State(0, "s0", false)) {

    private val _states: MutableSet<State> = mutableSetOf(startState)
    val states: Set<State>
        get() = _states

    fun addState(state: State): State {
        if (containsState(state)) {
            throw DuplicatedStateException(state)
        }
        _states.add(state)
        return state
    }

    fun getState(stateId: Int): State? {
        return _states.firstOrNull { it.id == stateId }
    }

    fun containsState(state: State): Boolean {
        return _states.firstOrNull { it.id == state.id } != null
    }

    @Suppress("PropertyName")
    protected open val _edges: MutableMap<State, Edge<T?>> = mutableMapOf()
    val edges: MutableMap<State, Edge<T?>>
        get() = _edges

    val stateMap: Map<State, Map<State, Set<T?>>>
        get() {
            val newEdgeList = mutableMapOf<State, MutableMap<State, MutableSet<T?>>>()
            states.forEach { firstState ->

                val firstStateEdges = newEdgeList[firstState] ?: mutableMapOf()
                edges[firstState]?.forEach {

                    val transition = it.key
                    it.value.forEach { lastState ->
                        val transitions = firstStateEdges[lastState] ?: mutableSetOf()
                        transitions.add(transition)
                        firstStateEdges[lastState] = transitions
                    }
                    newEdgeList[firstState] = firstStateEdges
                }
            }
            return newEdgeList.mapValues {
                it.value.mapValues {
                    it.value.toSet()
                }.toMap()
            }
        }

    fun containsEdge(start: State, transition: T?, endState: State): Boolean {
        return (_edges[start] ?: mutableMapOf())[transition]?.any { endState.id == it.id } ?: false
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
            val nextStates = _edges[currentState]?.next(firstCharacter)
            val nextStatesWithLambda = _edges[currentState]?.next(null)
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



