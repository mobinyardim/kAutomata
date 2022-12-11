package com.mobinyardim.libs.kautomata.edge

import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException
import com.mobinyardim.libs.kautomata.exceptions.NoSuchEdgeException

class MutableEdges<T : Enum<T>> : Edges<T>() {

    fun addEdge(edge: Edge<T>) {
        if (_edges.contains(edge)) {
            throw DuplicatedEdgeException(edge)
        }
        _edges.add(edge)
    }

    fun removeEdge(edge: Edge<T>) {
        if (_edges.contains(edge)) {
            _edges.remove(edge)
        } else {
            throw NoSuchEdgeException()
        }
    }

    fun toEdge(): Edges<T> {
        return Edges(edges)
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
        removeEdge(edge)
    }

}