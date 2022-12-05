package com.mobinyardim.libs.kautomata

import com.google.common.truth.Truth
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException
import com.mobinyardim.libs.kautomata.exceptions.NoSuchStateException
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test

internal class DFATest{

    @Test
    fun `addEdge when edges empty must correctly add edge`() {
        val automata = DFA<AutomataTest.Language>()

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

        val transition = AutomataTest.Language.a
        automata.addEdge(state1, transition, state2)

        Truth.assertThat(
            automata.containsEdge(
                state1,
                transition,
                state2
            )
        ).isTrue()
    }

    @Test
    fun `addEdge when state have edge must add another edge`() {
        val automata = DFA<AutomataTest.Language>()

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

        val transition1 = AutomataTest.Language.a
        val transition2 = AutomataTest.Language.b

        automata.addEdge(state1, transition1, state2)
        automata.addEdge(state1, transition2, state2)

        Truth.assertThat(
            automata.containsEdge(
                state1,
                transition1,
                state2
            )
        ).isTrue()

        Truth.assertThat(
            automata.containsEdge(
                state1,
                transition2,
                state2
            )
        ).isTrue()
    }

    @Test
    fun `addEdge when add different endState for same transition that has end state old one must be overwritten`() {
        val automata = DFA<AutomataTest.Language>()

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

        val transition1 = AutomataTest.Language.a
        val transition2 = AutomataTest.Language.b

        automata.addEdge(state1, transition1, state2)

        Truth.assertThat(
            automata.containsEdge(
                state1,
                transition1,
                state2
            )
        ).isTrue()

        automata.addEdge(state1, transition1, state1)

        Truth.assertThat(
            automata.containsEdge(
                state1,
                transition1,
                state2
            )
        ).isFalse()

        Truth.assertThat(
            automata.containsEdge(
                state1,
                transition1,
                state1
            )
        ).isTrue()
    }

    @Test
    fun `addEdge when add edge to state is not in states must throw exception`() {
        val automata = DFA<AutomataTest.Language>()

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

        val transition1 = AutomataTest.Language.a

        assertThrows<NoSuchStateException> {
            automata.addEdge(state1, transition1, state2)
        }
    }

    @Test
    fun `addEdge when add duplicated edge add must throw exception`() {
        val automata = DFA<AutomataTest.Language>()

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        automata.addState(state = state1)

        val transition1 = AutomataTest.Language.a
        automata.addEdge(state1, transition1, state1)

        assertThrows<DuplicatedEdgeException> {
            automata.addEdge(state1, transition1, state1)
        }
    }
}