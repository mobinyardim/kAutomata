package com.mobinyardim.libs.kautomata.edge

import com.mobinyardim.libs.kautomata.State

data class Edge<T : Enum<T>>(
    val start: State,
    val end: State,
    val transition: T?
)