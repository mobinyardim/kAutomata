package com.mobinyardim.libs.kautomata

class DuplicatedStateException(state: State) : Exception("There is state with ${state.id} already exist!")