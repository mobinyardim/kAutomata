package com.mobinyardim.libs.kautomata

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class AutomataExtKtTest {

    companion object {
        val stateId0 = 0
        val stateName0 = "s0"
        val isFinalState0 = false
        val state0 = State(
            id = stateId0,
            name = stateName0,
            isFinal = isFinalState0
        )

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

        fun generateAutomata(): Automata<AutomataTest.Language> {
            val automata = object : Automata<AutomataTest.Language>(state1) {
                fun addEdge(
                    startState: State,
                    transition: AutomataTest.Language?,
                    endState: State
                ) {
                    _addEdge(
                        startState, transition, endState
                    )
                }
            }

            automata.addState(state2)
            automata.addEdge(state1, AutomataTest.Language.a, state2)
            automata.addEdge(state1, AutomataTest.Language.b, state2)
            automata.addEdge(state1, null, state2)

            return automata
        }

        fun generateAutomata2(): Automata<AutomataTest.Language> {
            val automata = object : Automata<AutomataTest.Language>(state0) {
                fun addEdge(
                    startState: State,
                    transition: AutomataTest.Language?,
                    endState: State
                ) {
                    _addEdge(
                        startState, transition, endState
                    )
                }
            }

            automata.addState(state1)
            automata.addState(state2)
            automata.addEdge(state0, AutomataTest.Language.a, state1)
            automata.addEdge(state1, AutomataTest.Language.b, state2)
            automata.addEdge(state2, null, state0)

            return automata
        }
    }


    @Test
    fun `reverseEdges when there is not must reverse edges must return exact same map`() {
        val automata = generateAutomata()

        val reversedEdges = automata.reverseEdges(mutableMapOf())

        assertThat(
            reversedEdges
        ).isEqualTo(automata.edges)
    }


    @Test
    fun `reverseEdges when there is some edges must all edges reversed`() {
        val automata = generateAutomata()

        val edge = mutableMapOf<State, Map<State, Set<AutomataTest.Language?>>>().apply {
            put(state1, mapOf(state2 to setOf(AutomataTest.Language.b, null)))
            put(state2, mapOf(state1 to setOf(AutomataTest.Language.a)))
        }
        val reversedEdges = automata.reverseEdges(
            mutableMapOf<State, Map<State, Set<AutomataTest.Language?>>>().apply {
                put(state1, mapOf(state2 to setOf(AutomataTest.Language.a)))
            }
        )

        assertThat(
            reversedEdges
        ).isEqualTo(edge)
    }

    @Test
    fun `reverseEdges when incoming and outgoing edge of two state overlapping must all edges reversed`() {
        val automata = generateAutomata2()

        val edge = mutableMapOf<State, Map<State, Set<AutomataTest.Language?>>>().apply {
            put(
                state1,
                mapOf(
                    state0 to setOf(AutomataTest.Language.a),
                    state2 to setOf(AutomataTest.Language.b)
                )
            )
            put(state2, mapOf(state0 to setOf(null)))
        }
        val reversedEdges = automata.reverseEdges(
            mutableMapOf<State, Map<State, Set<AutomataTest.Language?>>>().apply {
                put(state0, mapOf(state1 to setOf(AutomataTest.Language.a)))
            }
        )

        assertThat(
            reversedEdges
        ).isEqualTo(edge)
    }

}