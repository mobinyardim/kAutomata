interface AutomataStateTracer<T> {
    fun onStart()

    fun onCurrentStateChange(state: State)

    fun onFinalState(state: State)

    fun onTrap(state: State, notConsumedString: List<T>)
}