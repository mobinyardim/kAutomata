package com.mobinyardim.libs.kautomata.edge

import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException

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

    fun toEdge(): Edges<T> {
        return copy()
    }

    override fun hashCode(): Int {
        return _edges.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Edges<*> -> {
                _edges == other.edges
            }
            is MutableEdges<*> -> {
                _edges == other._edges
            }
            else -> {
                super.equals(other)
            }
        }
    }

    /**
     *Operators
     **/
    operator fun plusAssign(edges: Edges<T>) {
        edges.edges.forEach {
            if (!contain(it)) {
                addEdge(it)
            }
        }
    }

    operator fun minusAssign(edges: Edges<T>) {
        edges.edges.forEach {
            if (contain(it)) {
                removeEdge(it)
            }
        }
    }

    operator fun plusAssign(edge: Edge<T>) {
        addEdge(edge)
    }

    operator fun minusAssign(edge: Edge<T>) {
        addEdge(edge)
    }

}