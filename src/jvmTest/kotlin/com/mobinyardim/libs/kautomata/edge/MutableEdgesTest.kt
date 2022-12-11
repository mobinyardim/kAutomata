package com.mobinyardim.libs.kautomata.edge

import com.google.common.truth.Truth
import com.mobinyardim.libs.kautomata.Language
import com.mobinyardim.libs.kautomata.State
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException
import com.mobinyardim.libs.kautomata.exceptions.NoSuchEdgeException
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

internal class MutableEdgesTest {

    @Test
    fun `addEdge when edges empty must correctly add edge`() {
        val edges = MutableEdges<Language>()

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


        val transition = Language.a
        val edge = Edge(start = state1, transition = transition, end = state2)
        edges.addEdge(edge)

        Truth.assertThat(
            edges.contain(edge)
        ).isTrue()
    }

    @Test
    fun `addEdge when state have edge must add another edge`() {
        val edges = MutableEdges<Language>()

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

        val transition1 = Language.a
        val transition2 = Language.b

        val edge1 = Edge(start = state1, transition = transition1, end = state2)
        val edge2 = Edge(start = state1, transition = transition2, end = state2)

        edges.addEdge(edge1)
        edges.addEdge(edge2)

        Truth.assertThat(
            edges.contain(
                edge1
            )
        ).isTrue()

        Truth.assertThat(
            edges.contain(edge2)
        ).isTrue()
    }

    @Test
    fun `addEdge when add duplicated edge add must throw exception`() {
        val edges = MutableEdges<Language>()

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )


        val transition1 = Language.a
        val edge = Edge(start = state1, transition = transition1, end = state1)

        edges.addEdge(edge)

        org.junit.jupiter.api.assertThrows<DuplicatedEdgeException> {
            edges.addEdge(edge)
        }
    }

    @Test
    fun `removeEdge when remove not lambda edge after remove must edge will be removed`() {
        val edges = MutableEdges<Language>()

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val transition1 = Language.a
        val edge1 = Edge(start = state1, transition = transition1, end = state1)

        edges.addEdge(edge1)

        Truth.assertThat(
            edges.contain(edge1)
        ).isTrue()

        edges.removeEdge(edge1)

        Truth.assertThat(
            edges.contain(edge1)
        ).isFalse()
    }

    @Test
    fun `removeEdge when remove lambda edge after remove must edge will be removed`() {
        val edges = MutableEdges<Language>()

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val transition1: Language? = null
        val edge = Edge(start = state1, transition = transition1, end = state1)
        edges.addEdge(edge)

        Truth.assertThat(
            edges.contain(edge)
        ).isTrue()

        edges.removeEdge(edge)
        Truth.assertThat(
            edges.contain(edge)
        ).isFalse()
    }

    @Test
    fun `removeEdge when there is not such edge`() {
        val edges = MutableEdges<Language>()

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val transition1: Language? = null
        val edge = Edge(start = state1, transition = transition1, end = state1)

        org.junit.jupiter.api.assertThrows<NoSuchEdgeException> {
            edges.removeEdge(edge)
        }
    }

    @Test
    fun toEdge() {
    }

    @Test
    fun plusAssign() {
    }

    @Test
    fun minusAssign() {
    }

    @Test
    fun testPlusAssign() {
    }

    @Test
    fun testMinusAssign() {
    }
}