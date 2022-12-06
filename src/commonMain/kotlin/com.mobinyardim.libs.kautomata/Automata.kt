package com.mobinyardim.libs.kautomata

import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedStateException
import com.mobinyardim.libs.kautomata.exceptions.NoSuchStateException
import com.mobinyardim.libs.kautomata.utils.removeIf

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

    @Suppress("FunctionName")
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

    fun removeEdge(startState: State, transition: T?, endState: State) {
        val startStateEdges = (_nextEdges[startState] ?: mapOf())
        val newEdges: Map<T?, Set<State>> = mutableMapOf<T?, Set<State>>().apply {
            startStateEdges.forEach {
                if (it.key != transition && !it.value.contains(endState)) {
                    put(it.key, it.value)
                }
            }
        }
        _nextEdges[startState] = newEdges

        if (_edges[startState]?.get(endState) != null &&
            _edges[startState]?.get(endState)?.contains(transition) == true
        ) {
            val startToEndTransitions = _edges[startState]!![endState] ?: setOf()
            _edges[startState]!![endState] = startToEndTransitions.filter {
                it != transition
            }.toSet()
        }
    }

    fun removeEdge(startState: State, transition: T?) {
        val startStateEdges = (_nextEdges[startState] ?: mapOf())
        val newNextEdges: Map<T?, Set<State>> = mutableMapOf<T?, Set<State>>().apply {
            startStateEdges.forEach {
                if (it.key != transition) {
                    put(it.key, it.value)
                }
            }
        }
        _nextEdges[startState] = newNextEdges

        val newEdges = _edges[startState]?.mapValues {
            if (!it.value.contains(transition))
                it.value
            else {
                it.value.filter {
                    it != transition
                }.toSet()
            }
        } ?: mapOf()
        _edges[startState] = newEdges.toMutableMap()
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

    fun removeCycles(
        algorithm: RemoveCycleAlgorithm = RemoveCycleAlgorithm.SIMPLE_HEURISTIC
    ): Map<State, Map<State, Set<T?>>> {
        return when (algorithm) {
            RemoveCycleAlgorithm.SIMPLE_HEURISTIC -> removeCyclesSimpleHeuristic()
        }
    }

    private fun removeCyclesSimpleHeuristic(): Map<State, Map<State, Set<T?>>> {
        val edges = _edges
        val mustReverseEdges: MutableMap<State, MutableMap<State, Set<T?>>> = mutableMapOf()
        states.forEach {
            val incomingEdges = incomingEdges(it, edges)
            val outGoingEdges = outgoingEdges(it, edges)
            mustReverseEdges += if (edgesCount(incomingEdges) >= edgesCount(outGoingEdges)) {
                outGoingEdges
            } else {
                incomingEdges
            }
            edges.removeIf { entry ->
                incomingEdges.contains(entry.key) ||
                        outGoingEdges.contains(entry.key)
            }
        }
        val newEdges: MutableMap<State, MutableMap<State, Set<T?>>> = mutableMapOf()
        _edges.forEach { entry ->
            val firstState = entry.key
            entry.value.forEach {
                val reversedEdge = mustReverseEdges[firstState]?.get(it.key)
                if (reversedEdge != null) {
                    newEdges[it.key] = mutableMapOf<State, Set<T?>>().apply {
                        put(firstState, reversedEdge)
                    }
                } else {
                    newEdges[firstState] = mutableMapOf<State, Set<T?>>().apply {
                        put(it.key, it.value)
                    }
                }
            }
        }

        return newEdges
    }

    fun incomingEdges(
        state: State,
        edges: Map<State, MutableMap<State, Set<T?>>> = _edges
    ): Map<State, MutableMap<State, Set<T?>>> {
        return edges.filterKeys {
            it != state
        }
    }

    fun outgoingEdges(
        state: State,
        edges: Map<State, MutableMap<State, Set<T?>>> = _edges
    ): Map<State, MutableMap<State, Set<T?>>> {
        return edges.filterKeys {
            it == state
        }
    }

    fun edgesCount(edges: Map<State, MutableMap<State, Set<T?>>> = _edges): Int {
        var sum = 0
        edges.forEach {
            it.value.forEach {
                sum += it.value.size
            }
        }
        return sum
    }

    enum class RemoveCycleAlgorithm {
        SIMPLE_HEURISTIC
    }
}



