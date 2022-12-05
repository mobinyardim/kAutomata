package com.mobinyardim.libs.kautomata.exceptions

import com.mobinyardim.libs.kautomata.State

class DuplicatedStateException(state: State) : Exception("There is state with ${state.id} already exist!")