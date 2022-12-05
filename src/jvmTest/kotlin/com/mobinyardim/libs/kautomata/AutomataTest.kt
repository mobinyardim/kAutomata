package com.mobinyardim.libs.kautomata

import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedStateException
import com.mobinyardim.libs.kautomata.exceptions.NoSuchStateException
import com.mobinyardim.libs.kautomata.utils.toEnumList
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

internal class AutomataTest {

    enum class Language {
        a,
        b
    }

    @Test
    fun `addState must return exactly added state`() {
        val automata = object : Automata<Language>() {}

        val stateId = 1
        val stateName = "s1"
        val isFinalState = true
        val state = State(
            id = stateId,
            name = stateName,
            isFinal = isFinalState
        )
        val s1 = automata.addState(state = state)

        assertThat(s1).isEqualTo(state)
    }

    @Test
    fun `addState when add duplicated state must throw exception`() {
        val automata = object : Automata<Language>() {}

        val stateId = 1
        val stateName = "s1"
        val isFinalState = true
        val state = State(
            id = stateId,
            name = stateName,
            isFinal = isFinalState
        )
        automata.addState(state = state)

        assertThrows<DuplicatedStateException> {
            automata.addState(state = state)
        }
    }

    @Test
    fun `addState when add duplicated state with different name and same id must throw exception`() {
        val automata = object : Automata<Language>() {}

        val stateId = 1
        val stateName = "s1"
        val isFinalState = true
        val state = State(
            id = stateId,
            name = stateName,
            isFinal = isFinalState
        )
        automata.addState(state = state.copy(name = "s3"))

        assertThrows<DuplicatedStateException> {
            automata.addState(state = state)
        }
    }

    @Test
    fun `getState must return exactly the added state`() {
        val automata = object : Automata<Language>() {}

        val stateId = 1
        val stateName = "s1"
        val isFinalState = true
        val state = State(
            id = stateId,
            name = stateName,
            isFinal = isFinalState
        )
        automata.addState(state = state)

        assertThat(automata.getState(stateId)).isEqualTo(state)
    }

    @Test
    fun `containsEdge must return false when there is no edge with lambda`() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val automata = object : Automata<Language>(state1) {}

        assertThat(
            automata.containsEdge(state1, null, state1)
        ).isFalse()
    }

    @Test
    fun `containsEdge must return false when there is edge with lambda`() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val automata = object : Automata<Language>(state1) {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }
        automata.addEdge(state1, null, state1)

        assertThat(
            automata.containsEdge(state1, null, state1)
        ).isTrue()
    }

    @Test
    fun `containsEdge must return true when there is edge with not lambda transition`() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val automata = object : Automata<Language>(state1) {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }
        val transition = Language.a
        automata.addEdge(state1, transition, state1)

        assertThat(
            automata.containsEdge(state1, transition, state1)
        ).isTrue()

    }

    @Test
    fun `containsEdge must return true when there is no edge with not lambda transition`() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val automata = object : Automata<Language>(state1) {}

        enumValues<Language>().forEach {
            assertThat(
                automata.containsEdge(state1, it, state1)
            ).isFalse()
        }
    }

    @Test
    fun `addEdge when edges empty must correctly add edge`() {
        val automata = object : Automata<Language>() {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val stateId2 = 2
        val stateName2 = "s2"
        val isFinalState2 = true
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )

        automata.addState(state = state1)
        automata.addState(state = state2)

        val transition = Language.a
        automata.addEdge(state1, transition, state2)

        assertThat(
            automata.containsEdge(
                state1,
                transition,
                state2
            )
        ).isTrue()
    }

    @Test
    fun `addEdge when state have edge must add another edge`() {
        val automata = object : Automata<Language>() {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val stateId2 = 2
        val stateName2 = "s2"
        val isFinalState2 = true
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )

        automata.addState(state = state1)
        automata.addState(state = state2)

        val transition1 = Language.a
        val transition2 = Language.b

        automata.addEdge(state1, transition1, state2)
        automata.addEdge(state1, transition2, state2)

        assertThat(
            automata.containsEdge(
                state1,
                transition1,
                state2
            )
        ).isTrue()

        assertThat(
            automata.containsEdge(
                state1,
                transition2,
                state2
            )
        ).isTrue()
    }

    @Test
    fun `addEdge when add edge to state is not in states must throw exception`() {
        val automata = object : Automata<Language>() {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val stateId2 = 2
        val stateName2 = "s2"
        val isFinalState2 = true
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )

        automata.addState(state = state1)

        val transition1 = Language.a

        assertThrows<NoSuchStateException> {
            automata.addEdge(state1, transition1, state2)
        }
    }

    @Test
    fun `addEdge when add duplicated edge add must throw exception`() {
        val automata = object : Automata<Language>() {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        automata.addState(state = state1)

        val transition1 = Language.a
        automata.addEdge(state1, transition1, state1)

        assertThrows<DuplicatedEdgeException> {
            automata.addEdge(state1, transition1, state1)
        }
    }

    @Test
    fun `removeEdge when remove not lambda edge after remove must edge will be removed`() {
        val automata = object : Automata<Language>() {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        automata.addState(state = state1)

        val transition1 = Language.a
        automata.addEdge(state1, transition1, state1)
        assertThat(
            automata.containsEdge(state1, transition1, state1)
        ).isTrue()

        automata.removeEdge(state1, transition1, state1)
        assertThat(
            automata.containsEdge(state1, transition1, state1)
        ).isFalse()
    }

    @Test
    fun `removeEdge when remove lambda edge after remove must edge will be removed`() {
        val automata = object : Automata<Language>() {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        automata.addState(state = state1)

        val transition1 = null
        automata.addEdge(state1, transition1, state1)
        assertThat(
            automata.containsEdge(state1, transition1, state1)
        ).isTrue()

        automata.removeEdge(state1, transition1, state1)
        assertThat(
            automata.containsEdge(state1, transition1, state1)
        ).isFalse()
    }

    @Test
    fun `removeEdge when remove edge with start and lambda transition must all edges deleted`() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val stateId2 = 2
        val stateName2 = "s2"
        val isFinalState2 = false
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )

        val automata = object : Automata<Language>(state1) {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }


        automata.addState(state = state2)

        val transition = null

        automata.addEdge(state1, transition, state1)
        assertThat(
            automata.containsEdge(state1, transition, state1)
        ).isTrue()

        automata.addEdge(state1, transition, state2)
        assertThat(
            automata.containsEdge(state1, transition, state2)
        ).isTrue()

        automata.removeEdge(state1, transition)
        assertThat(
            automata.containsEdge(state1, transition, state1)
        ).isFalse()
        assertThat(
            automata.containsEdge(state1, transition, state2)
        ).isFalse()
    }

    @Test
    fun `removeEdge when remove edge with start and transition must all edges deleted`() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val stateId2 = 2
        val stateName2 = "s2"
        val isFinalState2 = false
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )

        val automata = object : Automata<Language>(state1) {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }


        automata.addState(state = state2)

        val transition = Language.a

        automata.addEdge(state1, transition, state1)
        assertThat(
            automata.containsEdge(state1, transition, state1)
        ).isTrue()

        automata.addEdge(state1, transition, state2)
        assertThat(
            automata.containsEdge(state1, transition, state2)
        ).isTrue()

        automata.removeEdge(state1, transition)
        assertThat(
            automata.containsEdge(state1, transition, state1)
        ).isFalse()
        assertThat(
            automata.containsEdge(state1, transition, state2)
        ).isFalse()
    }

    @Test
    fun `trace when called onStart must call just one time`() {
        val automata = NFA<Language>()

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val stateId2 = 2
        val stateName2 = "s2"
        val isFinalState2 = true
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )

        automata.addState(state = state1)
        automata.addState(state = state2)

        val transition1 = Language.a
        val transition2 = Language.b

        automata.addEdge(state1, transition1, state2)
        automata.addEdge(state1, transition2, state2)

        val onStart = mock<() -> Unit> { on { invoke() }.doReturn(Unit) }

        automata.trace(
            "aa".toEnumList(),
            automataStateTracer = object : AutomataStateTracer<Language> {
                override fun onStart() {
                    onStart.invoke()
                }

                override fun onCurrentStateChange(state: State) {}

                override fun onFinalState(state: State) {}

                override fun onTransition(start: State, transition: Language?, endState: State) {}

                override fun onTrap(state: State, notConsumedString: List<Language>) {}

            })

        verify(onStart, times(1)).invoke()
    }

    @Test
    fun `trace when called current state in first time must be first state`() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val automata = NFA<Language>(state1)

        val stateId2 = 2
        val stateName2 = "s2"
        val isFinalState2 = true
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )

        automata.addState(state = state2)

        val transition1 = Language.a
        val transition2 = Language.b

        automata.addEdge(state1, transition1, state2)
        automata.addEdge(state1, transition2, state2)

        val onStart = mock<() -> Unit> { on { invoke() }.doReturn(Unit) }
        val onCurrentStateChange =
            mock<(state: State) -> Unit> { on { invoke(any()) }.doReturn(Unit) }

        automata.trace(
            "aa".toEnumList(),
            automataStateTracer = object : AutomataStateTracer<Language> {
                override fun onStart() {
                    onStart.invoke()
                }

                override fun onCurrentStateChange(state: State) {
                    onCurrentStateChange.invoke(state)
                }

                override fun onFinalState(state: State) {}

                override fun onTransition(start: State, transition: Language?, endState: State) {}

                override fun onTrap(state: State, notConsumedString: List<Language>) {}

            })

        verify(onStart, times(1)).invoke()

        argumentCaptor<State>().apply {
            verify(onCurrentStateChange, atLeast(1)).invoke(capture())
            assertThat(firstValue).isEqualTo(state1)
        }
    }

    @Test
    fun `trace when called and there is just one state and state is final onFinalState must called `() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = true
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val automata = object : Automata<Language>(state1) {}

        val onStart = mock<() -> Unit> { on { invoke() }.doReturn(Unit) }
        val onFinalState = mock<(state: State) -> Unit> { on { invoke(any()) }.doReturn(Unit) }

        automata.trace(
            "".toEnumList(),
            automataStateTracer = object : AutomataStateTracer<Language> {
                override fun onStart() {
                    onStart.invoke()
                }

                override fun onCurrentStateChange(state: State) {
                }

                override fun onFinalState(state: State) {
                    onFinalState.invoke(state)
                }

                override fun onTransition(start: State, transition: Language?, endState: State) {}

                override fun onTrap(state: State, notConsumedString: List<Language>) {}

            })

        verify(onStart, times(1)).invoke()

        argumentCaptor<State>().apply {
            verify(onFinalState, times(1)).invoke(capture())
            assertThat(firstValue).isEqualTo(state1)
        }
    }

    @Test
    fun `trace when called and there is just one state and state is not final onTrap must called `() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val automata = object : Automata<Language>(state1) {}

        val onStart = mock<() -> Unit> { on { invoke() }.doReturn(Unit) }
        val onTrap = mock<(state: State) -> Unit> { on { invoke(any()) }.doReturn(Unit) }

        automata.trace(
            "".toEnumList(),
            automataStateTracer = object : AutomataStateTracer<Language> {
                override fun onStart() {
                    onStart.invoke()
                }

                override fun onCurrentStateChange(state: State) {
                }

                override fun onFinalState(state: State) {
                }

                override fun onTransition(start: State, transition: Language?, endState: State) {}

                override fun onTrap(state: State, notConsumedString: List<Language>) {
                    onTrap.invoke(state)
                }

            })

        verify(onStart, times(1)).invoke()

        argumentCaptor<State>().apply {
            verify(onTrap, times(1)).invoke(capture())
            assertThat(firstValue).isEqualTo(state1)
        }
    }
}