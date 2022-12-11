package com.mobinyardim.libs.kautomata.exceptions

import com.mobinyardim.libs.kautomata.edge.Edge

class DuplicatedEdgeException(
    edge: Edge<*>
) : Exception("Duplicated edge from ${edge.start.name} to ${edge.end.name} with ${edge.transition?.name ?: "lambda"}!")