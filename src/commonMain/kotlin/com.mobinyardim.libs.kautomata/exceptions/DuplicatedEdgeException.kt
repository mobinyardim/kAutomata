package com.mobinyardim.libs.kautomata.exceptions

import com.mobinyardim.libs.kautomata.State

class DuplicatedEdgeException(
    startState: State,
    transition: String,
    endState: State
) : Exception("Duplicated edge from ${startState.name} to ${endState.name} with ${transition}!")