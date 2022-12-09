package com.mobinyardim.libs.kautomata

import com.mobinyardim.libs.kautomata.edge.Edge
import com.mobinyardim.libs.kautomata.edge.Edges
import com.mobinyardim.libs.kautomata.edge.MutableEdges
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedStateException
import com.mobinyardim.libs.kautomata.exceptions.NoSuchStateException

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
    protected val _edges: MutableEdges<T> = MutableEdges()
    val edges: Edges<T>
        get() = _edges

    @Suppress("FunctionName")
    protected fun _addEdge(startState: State, transition: T?, endState: State) {

        if (!containsState(startState) || !containsState(endState))
            throw NoSuchStateException()

        _edges.addEdge(
            Edge(
                start = startState,
                transition = transition,
                end = endState
            )
        )
    }

    fun removeEdge(edge: Edge<T>) {
        _edges.removeEdge(edge)
    }

    fun removeEdge(startState: State, transition: T?) {
        val outgoingEdges = edges.outgoingEdges(startState)
        outgoingEdges.edges.forEach {
            if (it.transition == transition)
                _edges.removeEdge(it)
        }
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
            val nextStates = edges.nextStates(currentState, firstCharacter)
            val nextStatesWithLambda = edges.nextStates(currentState, null)

            if (nextStates.isEmpty()) {
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
            if (nextStatesWithLambda.isNotEmpty()) {
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
    ): Edges<T> {
        return when (algorithm) {
            RemoveCycleAlgorithm.SIMPLE_HEURISTIC -> removeCyclesSimpleHeuristic()
            RemoveCycleAlgorithm.ENHANCED_GREEDY_HEURISTIC -> TODO()
            RemoveCycleAlgorithm.DEPTH_FIRST_SEARCH -> removeCyclesWithDepthFirstSearch()
        }
    }

    private fun removeCyclesSimpleHeuristic(): Edges<T> {
        val edges = _edges.copy().toMutableEdges()

        val mustReverseEdges = MutableEdges<T>()
        states.forEach {
            val incomingEdges = edges.incomingEdges(it)
            val outGoingEdges = edges.outgoingEdges(it)
            mustReverseEdges += if (incomingEdges.edgesCount() >= outGoingEdges.edgesCount()) {
                outGoingEdges
            } else {
                incomingEdges
            }

            edges -= incomingEdges
            edges -= outGoingEdges
        }
        return _edges.reverseEdges(mustReverseEdges)
    }

    private fun removeCyclesWithDepthFirstSearch(): Edges<T> {
        val edges = _edges.copy().toMutableEdges()

        val mustReverseEdges = MutableEdges<T>()
        val markedStates = mutableSetOf<State>()
        val stack = mutableListOf<State>()

        states.forEach {
            removeCyclesWithDepthFirstSearch(
                state = it,
                edges = edges,
                mustReverseEdges = mustReverseEdges,
                markedStates = markedStates,
                stack = stack
            )
        }


        return _edges.reverseEdges(mustReverseEdges)
    }

    private fun removeCyclesWithDepthFirstSearch(
        state: State,
        edges: MutableEdges<T>,
        mustReverseEdges: MutableEdges<T>,
        markedStates: MutableSet<State>,
        stack: MutableList<State>
    ) {
        if (markedStates.contains(state)) {
            return
        }
        markedStates.add(state)
        stack.add(state)

        val incomingEdges = edges.incomingEdges(state)

        incomingEdges.edges.forEach { edge ->
            if (stack.contains(edge.end)) {
                mustReverseEdges += edge
                edges.removeEdge(edge)
            } else {
                if (!markedStates.contains(edge.end)) {
                    removeCyclesWithDepthFirstSearch(
                        state = edge.end,
                        edges = edges,
                        mustReverseEdges = mustReverseEdges,
                        markedStates = markedStates,
                        stack = stack
                    )
                }
            }

        }
        stack.removeAt(stack.size - 1)
    }

    enum class RemoveCycleAlgorithm {
        SIMPLE_HEURISTIC,
        ENHANCED_GREEDY_HEURISTIC,
        DEPTH_FIRST_SEARCH
    }
}


