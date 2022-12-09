package com.mobinyardim.libs.kautomata.edge

import com.mobinyardim.libs.kautomata.State


open class Edges<T : Enum<T>> {

    @Suppress("PropertyName")
    protected val _edges: MutableSet<Edge<T>> = mutableSetOf()
    val edges: Set<Edge<T>>
        get() = _edges

    fun edgesCount(): Int {
        return edges.size
    }

    fun contain(edge: Edge<T>): Boolean {
        return edges.contains(edge)
    }

    fun incomingEdges(state: State): Edges<T> {
        val incomingEdges = MutableEdges<T>()
        _edges.forEach {
            if (it.end == state) {
                incomingEdges.addEdge(it)
            }
        }
        return incomingEdges
    }

    fun outgoingEdges(state: State): Edges<T> {
        val outGoingEdges = MutableEdges<T>()
        _edges.forEach {
            if (it.start == state) {
                outGoingEdges.addEdge(it)
            }
        }
        return outGoingEdges
    }

    fun nextStates(state: State, transition: T?): Set<State> {
        return edges.filter {
            it.start == state && it.transition == transition
        }.map {
            it.end
        }.toSet()
    }

    fun reverseEdges(mustReverseEdges: Edges<T>): Edges<T> {
        val newEdges = MutableEdges<T>()
        edges.forEach {
            if (mustReverseEdges.contain(it)) {
                newEdges.addEdge(edge = it.copy(start = it.end, end = it.start))
            } else {
                newEdges.addEdge(edge = it)
            }
        }
        return newEdges
    }

    fun toMutableEdges(): MutableEdges<T> {
        return MutableEdges<T>().apply {
            _edges.forEach {
                this.addEdge(it)
            }
        }
    }

    fun copy(): Edges<T> {
        return MutableEdges<T>().apply {
            this@Edges.edges.forEach {
                this@apply.addEdge(it)
            }
        }
    }

    override fun hashCode(): Int {
        return edges.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Edges<*> -> {
                _edges == other._edges
            }
            is MutableEdges<*> -> {
                _edges == other._edges
            }
            else -> {
                super.equals(other)
            }
        }
    }
}