package com.mobinyardim.libs.kautomata

fun <T : Enum<T>> Automata<T>.reverseEdges(mustReverseEdges: Map<State, Map<State, Set<T?>>>): Map<State, Map<State, Set<T?>>> {
    val newEdges: MutableMap<State, Map<State, Set<T?>>> = mutableMapOf()
    this.edges.forEach { entry ->
        val firstState = entry.key
        entry.value.forEach {
            val reversedEdge = mustReverseEdges[firstState]?.get(it.key)
            if (reversedEdge != null && reversedEdge.isNotEmpty()) {
                newEdges[it.key] = mutableMapOf<State, Set<T?>>().apply {
                    put(firstState, reversedEdge)
                }
                newEdges[firstState] = mutableMapOf<State, Set<T?>>().apply {
                    put(it.key, it.value.filter { !reversedEdge.contains(it) }.toSet())
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
