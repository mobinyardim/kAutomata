package com.mobinyardim.libs.kautomata.edge

import com.google.common.truth.Truth
import com.mobinyardim.libs.kautomata.Language
import com.mobinyardim.libs.kautomata.State
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class EdgesTest {

    @Test
    fun `containsEdge must return false when there is no edge with lambda`() {
        val edges = MutableEdges<Language>()

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )
        val edge = Edge<Language>(start = state1, transition = null, end = state1)

        Truth.assertThat(
            edges.contain(edge)
        ).isFalse()
    }

    @Test
    fun `containsEdge must return true when there is edge with lambda`() {
        val edges = MutableEdges<Language>()

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )
        val edge = Edge<Language>(start = state1, transition = null, end = state1)
        edges.addEdge(edge)

        Truth.assertThat(
            edges.contain(edge)
        ).isTrue()
    }

    @Test
    fun `containsEdge must return true when there is edge with not lambda transition`() {
        val edges = MutableEdges<Language>()

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )
        val transition = Language.a
        val edge = Edge(start = state1, transition = transition, end = state1)

        edges.addEdge(edge)

        Truth.assertThat(
            edges.contain(edge)
        ).isTrue()

    }

    @Test
    fun `containsEdge must return false when there is no edge with not lambda transition`() {
        val edges = MutableEdges<Language>()

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        enumValues<Language>().forEach {
            Truth.assertThat(
                edges.contain(edge = Edge(start = state1, transition = it, end = state1))
            ).isFalse()
        }
    }

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

        assertThrows<DuplicatedEdgeException> {
            edges.addEdge(edge)
        }
    }

    @Test
    fun `nextEdges must return correct values after add some edge`() {
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
            edges.contain(edge1)
        ).isTrue()

        Truth.assertThat(
            edges.contain(edge2)
        ).isTrue()

        val state1NextEdges = edges.nextStates(state1, transition1)
        Truth.assertThat(
            state1NextEdges.isNotEmpty()
        ).isTrue()

        Truth.assertThat(
            state1NextEdges
        ).isEqualTo(setOf(state2))

        val state1NextEdges2 = edges.nextStates(state1, transition2)
        Truth.assertThat(
            state1NextEdges2.isNotEmpty()
        ).isTrue()

        Truth.assertThat(
            state1NextEdges2
        ).isEqualTo(setOf(state2))

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
    fun `edgeCount when there is no edge must return 0`() {
        val edges = MutableEdges()
        Truth.assertThat(
            edges.edgesCount()
        ).isEqualTo(0)
    }

    @Test
    fun `edgeCount when there is one transition between two state must return 1 `() {
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
        val isFinalState2 = false
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )
        val edge = Edge(start = state1, transition = Language.a, end = state2)

        edges.addEdge(edge)

        Truth.assertThat(
            edges.edgesCount()
        ).isEqualTo(1)
    }

    @Test
    fun `edgeCount when there is multiple transition between two state must return exact count`() {
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
        val isFinalState2 = false
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )

        val edge1 = Edge(start = state1, transition = Language.a, end = state2)
        val edge2 = Edge(start = state1, transition = Language.b, end = state2)
        val edge3 = Edge<Language>(start = state1, transition = null, end = state2)

        edges.addEdge(edge1)
        edges.addEdge(edge2)
        edges.addEdge(edge3)

        Truth.assertThat(
            edges.edgesCount()
        ).isEqualTo(3)
    }

    @Test
    fun `incomingEdges when there is no incoming edge to state must return empty list`() {
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
        val edge1 = Edge(start = state1, transition = transition1, end = state2)

        edges.addEdge(edge1)

        val transition2 = Language.b
        val edge2 = Edge(start = state1, transition = transition2, end = state2)

        edges.addEdge(edge2)

        val incomingEdges = edges.incomingEdges(state1)

        Truth.assertThat(
            incomingEdges.edgesCount()
        ).isEqualTo(0)
    }

    @Test
    fun `incomingEdges when there is incoming edge to state must return all edges`() {
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
        val edge1 = Edge(start = state1, transition = transition1, end = state2)

        edges.addEdge(edge1)

        val transition2 = Language.b
        val edge2 = Edge(start = state1, transition = transition2, end = state2)

        edges.addEdge(edge2)

        val incomingEdges = edges.incomingEdges(state2)

        Truth.assertThat(
            incomingEdges.edgesCount()
        ).isEqualTo(2)

        Truth.assertThat(
            incomingEdges.contain(edge1)
        ).isTrue()

        Truth.assertThat(
            incomingEdges.contain(edge2)
        ).isTrue()
    }

    @Test
    fun `incomingEdges when there is incoming edge  and there is another edges to state must return all edges`() {
        val edges = MutableEdges<Language>()

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
        val isFinalState2 = true
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )

        val transition0 = Language.a
        val edge0 = Edge(start = state0, transition = transition0, end = state1)

        val transition1 = Language.a
        val edge1 = Edge(start = state1, transition = transition1, end = state2)

        val transition2 = Language.a
        val edge2 = Edge(start = state2, transition = transition2, end = state0)

        edges.addEdge(edge0)
        edges.addEdge(edge1)
        edges.addEdge(edge2)

        val incomingEdges = edges.incomingEdges(state2)

        Truth.assertThat(
            incomingEdges.edgesCount()
        ).isEqualTo(1)

        Truth.assertThat(
            incomingEdges.contain(edge1)
        ).isTrue()
    }

    @Test
    fun `incomingEdges when start state has another edges must just return edges with destination of given state`() {
        val edges = MutableEdges<Language>()

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
        val isFinalState2 = true
        val state2 = State(
            id = stateId2,
            name = stateName2,
            isFinal = isFinalState2
        )

        val transition0 = Language.a
        val edge0 = Edge(start = state0, transition = transition0, end = state1)

        val transition1 = Language.a
        val edge1 = Edge(start = state1, transition = transition1, end = state2)
        val edge2 = Edge(start = state1, transition = transition1, end = state0)

        val transition2 = Language.a
        val edge3 = Edge(start = state2, transition = transition2, end = state0)


        edges.addEdge(edge0)
        edges.addEdge(edge1)
        edges.addEdge(edge2)
        edges.addEdge(edge3)

        val incomingEdges = edges.incomingEdges(state2)

        Truth.assertThat(
            incomingEdges.edgesCount()
        ).isEqualTo(1)

        Truth.assertThat(
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
        val edge1 = Edge(start = state1, transition = transition1, end = state2)

        val transition2 = Language.b
        val edge2 = Edge(start = state1, transition = transition2, end = state2)

        edges.addEdge(edge1)
        edges.addEdge(edge2)

        val outgoingEdges = edges.outgoingEdges(state2)

        Truth.assertThat(
            outgoingEdges.edgesCount()
        ).isEqualTo(0)
    }

    @Test
    fun `outgoingEdges when there is incoming edge to state must return all edges`() {
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
        val edge1 = Edge(start = state1, transition = transition1, end = state2)


        val transition2 = Language.b
        val edge2 = Edge(start = state1, transition = transition2, end = state2)

        edges.addEdge(edge1)
        edges.addEdge(edge2)

        val outgoingEdges = edges.outgoingEdges(state1)

        Truth.assertThat(
            outgoingEdges.edgesCount()
        ).isEqualTo(2)

        Truth.assertThat(
            outgoingEdges.contain(edge1)
        ).isTrue()

        Truth.assertThat(
            outgoingEdges.contain(edge2)
        ).isTrue()
    }

}