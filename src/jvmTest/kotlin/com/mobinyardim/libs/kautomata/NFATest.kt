package com.mobinyardim.libs.kautomata

import com.google.common.truth.Truth
import com.mobinyardim.libs.kautomata.edge.Edge
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException
import com.mobinyardim.libs.kautomata.exceptions.NoSuchStateException
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test

internal class NFATest {

    @Test
    fun `addEdge when edges empty must correctly add edge`() {
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

        val transition = Language.a
        val edge = Edge(start = state1, transition = transition, end = state2)
        automata.addEdge(edge)

        Truth.assertThat(
            automata.edges.contain(edge)
        ).isTrue()
    }

    @Test
    fun `addEdge when state have edge must add another edge`() {
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

        val edge1 = Edge(start = state1, transition = transition1, end = state2)
        val edge2 = Edge(start = state1, transition = transition2, end = state2)

        automata.addEdge(edge1)
        automata.addEdge(edge2)

        Truth.assertThat(
            automata.edges.contain(
                Edge(
                    start = state1,
                    transition = transition1,
                    end = state2
                )
            )
        ).isTrue()

        Truth.assertThat(
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
    fun `addEdge when add edge to state is not in states must throw exception`() {
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

        val transition1 = Language.a

        assertThrows<NoSuchStateException> {
            automata.addEdge(Edge(start = state1, transition = transition1, end = state2))
        }
    }

    @Test
    fun `addEdge when add duplicated edge add must throw exception`() {
        val automata = NFA<Language>()

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
        val edge1 = Edge(start = state1, transition = transition1, end = state1)

        automata.addEdge(edge1)

        assertThrows<DuplicatedEdgeException> {
            automata.addEdge(edge1)
        }
    }
}