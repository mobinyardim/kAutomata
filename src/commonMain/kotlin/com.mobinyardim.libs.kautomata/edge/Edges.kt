package com.mobinyardim.libs.kautomata.edge

import com.mobinyardim.libs.kautomata.State
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException


open class Edges<T : Enum<T>> {


    @Suppress("PropertyName")
    protected val _edges: MutableSet<Edge<T>> = mutableSetOf()
    val edges: Set<Edge<T>>
        get() = _edges

    fun incomingEdges(state: State): Edges<T> {
        val incomingEdges = MutableEdges<T>()
        _edges.forEach {
            if (it.end == state) {
                incomingEdges.addEdge(it)
            }
        }
        return incomingEdges
    }

    fun outGoingEdges(state: State): Edges<T> {
        val outGoingEdges = MutableEdges<T>()
        _edges.forEach {
            if (it.start == state) {
                outGoingEdges.addEdge(it)
            }
        }
        return outGoingEdges
    }

    fun edgesCount(): Int {
        return edges.size
    }
}

class MutableEdges<T : Enum<T>> : Edges<T>() {

    fun addEdge(edge: Edge<T>) {
        if (_edges.contains(edge)) {
            throw DuplicatedEdgeException(
                edge.start, edge.transition?.name ?: "lambda", edge.end
            )
        }
        _edges.add(edge)
    }

    fun removeEdge(edge: Edge<T>) {
        if (_edges.contains(edge)) {
            _edges.remove(edge)
        }
    }
}