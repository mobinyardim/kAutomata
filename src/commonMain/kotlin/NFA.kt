class NFA<T : Enum<T>>(
    startState: State = State(0, "s0", false)
) : Automata<T>(startState)