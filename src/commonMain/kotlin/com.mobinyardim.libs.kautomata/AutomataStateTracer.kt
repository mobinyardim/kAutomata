package com.mobinyardim.libs.kautomata

interface AutomataStateTracer<T> {
    fun onStart()

    fun onCurrentStateChange(state: State)

    fun onFinalState(state: State)

    fun onTrap(state: State, notConsumedString: List<T>)

    fun onTransition(start: State, transition: T?, endState: State)
}