package com.mobinyardim.libs.kautomata

import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import com.mobinyardim.libs.kautomata.edge.Edge
import com.mobinyardim.libs.kautomata.edge.MutableEdges
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedStateException
import com.mobinyardim.libs.kautomata.exceptions.NoSuchStateException
import com.mobinyardim.libs.kautomata.utils.toEnumList
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*

internal class AutomataTest {

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
    fun `getState must return exactly the added states`() {


        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = true
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )
        val automata = object : Automata<Language>(state1) {}

        val stateId2 = 2
        val stateName2 = "s2"
        val isFinalState2 = false
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )
        val s2 = automata.addState(state = state2)

        assertThat(s2).isEqualTo(state2)
        assertThat(automata.states).isEqualTo(setOf(state1, state2))
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
            automata.edges.contain(
                Edge(start = state1, transition = null, end = state1)
            )
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
            automata.edges.contain(Edge(start = state1, transition = null, end = state1))
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
            automata.edges.contain(Edge(start = state1, transition = transition, end = state1))
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
                automata.edges.contain(edge = Edge(start = state1, transition = it, end = state1))
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
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition,
                    end = state2
                )
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
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition1,
                    end = state2
                )
            )
        ).isTrue()

        assertThat(
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition2,
                    end = state2
                )
            )
        ).isTrue()
    }

    @Test
    fun `addEdge when first state is not in automata states must throw exception`() {
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

        automata.addState(state = state2)

        val transition1 = Language.a

        assertThrows<NoSuchStateException> {
            automata.addEdge(state1, transition1, state2)
        }
    }

    @Test
    fun `addEdge when end state is not in automata states must throw exception`() {
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
    fun `nextEdges must return correct values after add some edge`() {
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
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition1,
                    end = state2
                )
            )
        ).isTrue()

        assertThat(
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition2,
                    end = state2
                )
            )
        ).isTrue()

        val state1NextEdges = automata.edges.nextStates(state1, transition1)
        assertThat(
            state1NextEdges.isNotEmpty()
        ).isTrue()

        assertThat(
            state1NextEdges
        ).isEqualTo(setOf(state2))

        val state1NextEdges2 = automata.edges.nextStates(state1, transition2)
        assertThat(
            state1NextEdges2.isNotEmpty()
        ).isTrue()

        assertThat(
            state1NextEdges2
        ).isEqualTo(setOf(state2))

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
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition1,
                    end = state1
                )
            )
        ).isTrue()

        automata.removeEdge(
            Edge(
                start = state1,
                transition = transition1,
                end = state1
            )
        )
        assertThat(
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition1,
                    end = state1
                )
            )
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

        val transition1: Language? = null
        val edge = Edge(start = state1, transition = transition1, end = state1)
        automata.addEdge(state1, transition1, state1)

        assertThat(
            automata.edges.contain(edge)
        ).isTrue()

        automata.removeEdge(edge)
        assertThat(
            automata.edges.contain(edge)
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
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition,
                    end = state1
                )
            )
        ).isTrue()

        automata.addEdge(state1, transition, state2)
        assertThat(
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition,
                    end = state2
                )
            )
        ).isTrue()

        automata.removeEdge(state1, transition)
        assertThat(
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition,
                    end = state1
                )
            )
        ).isFalse()
        assertThat(
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition,
                    end = state2
                )
            )
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
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition,
                    end = state1
                )
            )
        ).isTrue()

        automata.addEdge(state1, transition, state2)
        assertThat(
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition,
                    end = state2
                )
            )
        ).isTrue()

        automata.removeEdge(state1, transition)

        assertThat(
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition,
                    end = state1
                )
            )
        ).isFalse()
        assertThat(
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition,
                    end = state2
                )
            )
        ).isFalse()
    }

    @Test
    fun `edgeCount when there is no edge must return 0`() {
        val automata = object : Automata<Language>() {
            fun addEdge(startState: State, transition: Language?, endState: State) {
                _addEdge(
                    startState, transition, endState
                )
            }
        }
        assertThat(
            automata.edges.edgesCount()
        ).isEqualTo(0)
    }

    @Test
    fun `edgeCount when there is one transition between two state must return 1 `() {

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

        automata.addState(state2)
        automata.addEdge(state1, Language.a, state2)

        assertThat(
            automata.edges.edgesCount()
        ).isEqualTo(1)
    }

    @Test
    fun `edgeCount when there is multiple transition between two state must return exact count`() {

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

        automata.addState(state2)
        automata.addEdge(state1, Language.a, state2)
        automata.addEdge(state1, Language.b, state2)
        automata.addEdge(state1, null, state2)

        assertThat(
            automata.edges.edgesCount()
        ).isEqualTo(3)
    }

    @Test
    fun `incomingEdges when there is no incoming edge to state must return empty list`() {
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
        automata.addEdge(state1, transition1, state2)

        val transition2 = Language.b
        automata.addEdge(state1, transition2, state2)

        val incomingEdges = automata.edges.incomingEdges(state1)

        assertThat(
            incomingEdges.edgesCount()
        ).isEqualTo(0)
    }

    @Test
    fun `incomingEdges when there is incoming edge to state must return all edges`() {
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
        automata.addEdge(state1, transition1, state2)

        val transition2 = Language.b
        automata.addEdge(state1, transition2, state2)

        val incomingEdges = automata.edges.incomingEdges(state2)

        assertThat(
            incomingEdges.edgesCount()
        ).isEqualTo(2)

        assertThat(
            incomingEdges.contain(
                Edge(
                    start = state1,
                    transition = transition1,
                    end = state2
                )
            )
        ).isTrue()

        assertThat(
            incomingEdges.contain(
                Edge(
                    start = state1,
                    transition = transition2,
                    end = state2
                )
            )
        ).isTrue()
    }

    @Test
    fun `incomingEdges when there is incoming edge  and there is another edges to state must return all edges`() {
        val stateId0 = 0
        val stateName0 = "s0"
        val isFinalState0 = false
        val state0 = State(
            id = stateId0,
            name = stateName0,
            isFinal = isFinalState0
        )

        val automata = object : Automata<Language>(state0) {
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

        val transition0 = Language.a
        automata.addEdge(state0, transition0, state1)

        val transition1 = Language.a
        automata.addEdge(state1, transition1, state2)

        val transition2 = Language.a
        automata.addEdge(state2, transition2, state0)

        val incomingEdges = automata.edges.incomingEdges(state2)

        assertThat(
            incomingEdges.edgesCount()
        ).isEqualTo(1)

        assertThat(
            incomingEdges.contain(
                Edge(
                    start = state1,
                    transition = transition1,
                    end = state2
                )
            )
        ).isTrue()
    }

    @Test
    fun `incomingEdges when start state has another edges must just return edges with destination of given state`() {
        val stateId0 = 0
        val stateName0 = "s0"
        val isFinalState0 = false
        val state0 = State(
            id = stateId0,
            name = stateName0,
            isFinal = isFinalState0
        )

        val automata = object : Automata<Language>(state0) {
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

        val transition0 = Language.a
        automata.addEdge(state0, transition0, state1)

        val transition1 = Language.a
        automata.addEdge(state1, transition1, state2)
        automata.addEdge(state1, transition1, state0)

        val transition2 = Language.a
        automata.addEdge(state2, transition2, state0)

        val incomingEdges = automata.edges.incomingEdges(state2)

        assertThat(
            incomingEdges.edgesCount()
        ).isEqualTo(1)

        assertThat(
            incomingEdges.contain(
                Edge(
                    start = state1,
                    transition = transition1,
                    end = state2
                )
            )
        ).isTrue()
    }

    @Test
    fun `outgoingEdges when there is no incoming edge to state must return empty list`() {
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
        automata.addEdge(state1, transition1, state2)

        val transition2 = Language.b
        automata.addEdge(state1, transition2, state2)

        val outgoingEdges = automata.edges.outgoingEdges(state2)

        assertThat(
            outgoingEdges.edgesCount()
        ).isEqualTo(0)
    }

    @Test
    fun `outgoingEdges when there is incoming edge to state must return all edges`() {
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
        automata.addEdge(state1, transition1, state2)

        val transition2 = Language.b
        automata.addEdge(state1, transition2, state2)

        val outgoingEdges = automata.edges.outgoingEdges(state1)

        assertThat(
            outgoingEdges.edgesCount()
        ).isEqualTo(2)

        assertThat(
            outgoingEdges.contain(
                Edge(
                    start = state1,
                    transition = Language.a,
                    end = state2
                )
            )
        ).isTrue()

        assertThat(
            outgoingEdges.contain(
                Edge(
                    start = state1,
                    transition = Language.b,
                    end = state2
                )
            )
        ).isTrue()
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

    companion object {
        val a = State(id = 1, name = "a", isFinal = false)
        val b = State(id = 2, name = "b", isFinal = false)
        val c = State(id = 3, name = "c", isFinal = false)
        val d = State(id = 4, name = "d", isFinal = false)
        val e = State(id = 5, name = "e", isFinal = false)
        val f = State(id = 6, name = "f", isFinal = false)
        val g = State(id = 7, name = "g", isFinal = false)
        val h = State(id = 8, name = "h", isFinal = false)
        val transition = Language.a

        private fun generateAutomataForCycleRemove(): Automata<Language> {
            val automata = object : Automata<Language>(a) {
                fun addEdge(start: State, transition: Language, end: State) {
                    _addEdge(start, transition, end)
                }
            }

            automata.addState(b)
            automata.addState(c)
            automata.addState(d)
            automata.addState(e)
            automata.addState(f)
            automata.addState(g)
            automata.addState(h)

            automata.addEdge(a, transition, d)
            automata.addEdge(a, transition, b)
            automata.addEdge(a, transition, e)

            automata.addEdge(b, transition, h)

            automata.addEdge(c, transition, g)
            automata.addEdge(c, transition, a)

            automata.addEdge(e, transition, c)
            automata.addEdge(e, transition, g)

            automata.addEdge(f, transition, g)
            automata.addEdge(f, transition, e)

            automata.addEdge(h, transition, f)

            return automata
        }

        val automataAcyclicEdges = MutableEdges<Language>().apply {
            addEdge(Edge(start = a, transition = transition, end = d))
            addEdge(Edge(start = a, transition = transition, end = b))
            addEdge(Edge(start = a, transition = transition, end = e))
            addEdge(Edge(start = a, transition = transition, end = c))

            addEdge(Edge(start = b, transition = transition, end = h))

            addEdge(Edge(start = e, transition = transition, end = c))
            addEdge(Edge(start = e, transition = transition, end = g))

            addEdge(Edge(start = f, transition = transition, end = g))
            addEdge(Edge(start = f, transition = transition, end = e))

            addEdge(Edge(start = g, transition = transition, end = c))

            addEdge(Edge(start = h, transition = transition, end = f))
        }

        private fun generateAutomataForCycleRemove2(): Automata<Language> {
            val automata = object : Automata<Language>(a) {
                fun addEdge(start: State, transition: Language, end: State) {
                    _addEdge(start, transition, end)
                }
            }

            automata.addState(b)
            automata.addState(c)
            automata.addState(d)
            automata.addState(e)
            automata.addState(f)
            automata.addState(g)
            automata.addState(h)

            automata.addEdge(a, transition, d)
            automata.addEdge(a, transition, b)
            automata.addEdge(a, transition, e)

            automata.addEdge(b, transition, h)

            automata.addEdge(c, transition, a)

            automata.addEdge(e, transition, c)
            automata.addEdge(e, transition, g)

            automata.addEdge(f, transition, g)
            automata.addEdge(f, transition, e)

            automata.addEdge(h, transition, f)

            return automata
        }

        val automata2AcyclicEdges = MutableEdges<Language>().apply {
            addEdge(Edge(start = a, transition = transition, end = d))

            addEdge(Edge(start = b, transition = transition, end = h))
            addEdge(Edge(start = b, transition = transition, end = a))

            addEdge(Edge(start = c, transition = transition, end = a))

            addEdge(Edge(start = e, transition = transition, end = c))
            addEdge(Edge(start = e, transition = transition, end = a))
            addEdge(Edge(start = e, transition = transition, end = g))

            addEdge(Edge(start = f, transition = transition, end = g))
            addEdge(Edge(start = f, transition = transition, end = e))

            addEdge(Edge(start = h, transition = transition, end = f))
        }
    }

    @Test
    fun `removeCycles when called must automata edges not change`() {
        val automata = generateAutomataForCycleRemove()
        val oldEdges = automata.edges.copy()
        automata.removeCycles()
        assertThat(
            automata.edges.edges
        ).isEqualTo(
            oldEdges.edges
        )
    }


    @Test
    fun `removeCycle with heuristic algorithm`() {
        val automata = generateAutomataForCycleRemove()
        val acyclicEdges = automata.removeCycles()

        assertThat(
            automata.edges.edgesCount()
        ).isEqualTo(
            acyclicEdges.edgesCount()
        )

        assertThat(
            acyclicEdges.edges
        ).isEqualTo(
            automataAcyclicEdges.edges
        )

    }


    @Test
    fun `removeCycle with depth first algorithm`() {
        val automata = generateAutomataForCycleRemove2()
        val acyclicEdges = automata.removeCycles(Automata.RemoveCycleAlgorithm.DEPTH_FIRST_SEARCH)

        assertThat(
            automata.edges.edgesCount()
        ).isEqualTo(
            acyclicEdges.edgesCount()
        )

        assertThat(
            acyclicEdges.edges
        ).isEqualTo(
            automata2AcyclicEdges.edges
        )

    }
}
