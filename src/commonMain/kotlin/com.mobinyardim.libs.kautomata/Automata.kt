package com.mobinyardim.libs.kautomata

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

    /**
     * This field used for get next state with transition
     */
    @Suppress("PropertyName")
    private val _nextEdges: MutableMap<State, Map<T?, Set<State>>> = mutableMapOf()
    val nextEdges: MutableMap<State, Map<T?, Set<State>>>
        get() = _nextEdges

    /**
     *This field used for get transition between two state
     **/
    @Suppress("PropertyName")
    private val _edges: MutableMap<State, MutableMap<State, Set<T?>>> = mutableMapOf()
    val edges: Map<State, Map<State, Set<T?>>>
        get() = _edges

    protected fun _addEdge(startState: State, transition: T?, endState: State) {
        if (!containsState(startState) || !containsState(endState))
            throw NoSuchStateException()

        if (containsEdge(startState, transition, endState)) {
            throw DuplicatedEdgeException(
                startState, transition?.name ?: "lambda", endState
            )
        }

        val stateOutgoingEdges = _nextEdges[startState]?.toMutableMap() ?: mutableMapOf()
        val newOutGoingEdges = stateOutgoingEdges[transition]?.toMutableSet() ?: mutableSetOf()



        newOutGoingEdges.add(endState)
        stateOutgoingEdges[transition] = newOutGoingEdges

        if (_nextEdges[startState]?.contains(transition) == true) {
            _nextEdges.remove(startState)
            _nextEdges[startState] = stateOutgoingEdges
        } else {
            _nextEdges[startState] = stateOutgoingEdges
        }

        val startStateEdges = _edges[startState] ?: mutableMapOf()
        val startStateToEndStateEdges = startStateEdges[endState]?.toMutableSet() ?: mutableSetOf()

        startStateToEndStateEdges.add(transition)

        if (_edges.contains(startState)) {
            _edges[startState]!![endState] = startStateToEndStateEdges
        } else {
            _edges[startState] = mutableMapOf<State, Set<T?>>().apply {
                put(endState, startStateToEndStateEdges)
            }
        }

    }

    fun containsEdge(start: State, transition: T?, endState: State): Boolean {
        return ((_nextEdges[start] ?: mutableMapOf())[transition]?.any { endState.id == it.id }
            ?: false)
                && (_edges[start]?.get(endState)?.contains(transition) ?: false)
    }

    fun trace(string: List<T>, automataStateTracer: AutomataStateTracer<T>) {
        automataStateTracer.onStart()

        trace(string, automataStateTracer, startState)
    }


    private fun trace(
        string: List<T>,
        automataStateTracer: AutomataStateTracer<T>,
        currentState: State
    ) {
        automataStateTracer.onCurrentStateChange(currentState)

        if (string.isEmpty()) {
            if (currentState.isFinal) {
                automataStateTracer.onFinalState(currentState)
            } else {
                automataStateTracer.onTrap(currentState, string)
            }
        } else {
            val firstCharacter = string.first()
            val nextStates = _nextEdges[currentState]?.get(firstCharacter)
            val nextStatesWithLambda = _nextEdges[currentState]?.get(null)
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



