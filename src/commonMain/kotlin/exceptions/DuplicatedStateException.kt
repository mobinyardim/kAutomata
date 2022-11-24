package exceptions

import State

class DuplicatedStateException(state: State) : Exception("There is state with ${state.id} already exist!")