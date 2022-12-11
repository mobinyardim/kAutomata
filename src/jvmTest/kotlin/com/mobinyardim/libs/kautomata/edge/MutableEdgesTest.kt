package com.mobinyardim.libs.kautomata.edge

import com.google.common.truth.Truth
import com.mobinyardim.libs.kautomata.Language
import com.mobinyardim.libs.kautomata.State
import com.mobinyardim.libs.kautomata.exceptions.DuplicatedEdgeException
import com.mobinyardim.libs.kautomata.exceptions.NoSuchEdgeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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

        assertThrows<DuplicatedEdgeException> {
            edges.addEdge(edge)
        }
    }

    @Test
    fun `addEdge when there is edge between two state and add another edge must two edges available`() {
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
        val transition2 = Language.b

        val edge1 = Edge(start = state1, transition = transition1, end = state1)
        val edge2 = Edge(start = state1, transition = transition2, end = state1)

        edges.addEdge(edge1)
        edges.addEdge(edge2)

        Truth.assertThat(
            edges.edgesCount()
        ).isEqualTo(2)

        Truth.assertThat(
            edges.contain(edge1)
        ).isTrue()

        Truth.assertThat(
            edges.contain(edge2)
        ).isTrue()
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

        assertThrows<NoSuchEdgeException> {
            edges.removeEdge(edge)
        }
    }

    @Test
    fun `toEdge return exactly the same edges`() {
        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val edges = MutableEdges<Language>()
        edges.addEdge(Edge(start = state1, transition = Language.a, end = state1))

        Truth.assertThat(
            edges.edges
        ).isEqualTo(edges.toEdge().edges)
    }

    @Test
    fun `plusAssignOperatorEdges when there is no overlapping edges must union all edged`() {

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

        val edge1 = Edge(start = state1, transition = Language.a, end = state1)
        val edge2 = Edge(start = state1, transition = Language.b, end = state1)
        val edge3 = Edge(start = state1, transition = Language.a, end = state2)

        val edges1 = MutableEdges<Language>()
        edges1.addEdge(edge1)

        val edges2 = MutableEdges<Language>()
        edges2.addEdge(edge2)
        edges2.addEdge(edge3)

        edges1 += edges2

        Truth.assertThat(
            edges1.contain(edge1)
        ).isTrue()

        Truth.assertThat(
            edges1.contain(edge2)
        ).isTrue()

        Truth.assertThat(
            edges1.contain(edge3)
        ).isTrue()
    }

    @Test
    fun `plusAssignOperatorEdges when there is overlapping edges must ignore the overlapping edges`() {

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

        val edge1 = Edge(start = state1, transition = Language.a, end = state1)
        val edge2 = Edge(start = state1, transition = Language.b, end = state1)
        val edge3 = Edge(start = state1, transition = Language.a, end = state2)

        val edges1 = MutableEdges<Language>()
        edges1.addEdge(edge1)

        val edges2 = MutableEdges<Language>()

        edges2.addEdge(edge1)
        edges2.addEdge(edge2)
        edges2.addEdge(edge3)

        edges1 += edges2

        Truth.assertThat(
            edges1.contain(edge1)
        ).isTrue()

        Truth.assertThat(
            edges1.contain(edge2)
        ).isTrue()

        Truth.assertThat(
            edges1.contain(edge3)
        ).isTrue()
    }

    @Test
    fun `minusAssignEdgesOperator when there is overlapping edges must just remove overlapping edges `() {

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

        val edge1 = Edge(start = state1, transition = Language.a, end = state1)
        val edge2 = Edge(start = state1, transition = Language.b, end = state1)
        val edge3 = Edge(start = state1, transition = Language.a, end = state2)

        val edges1 = MutableEdges<Language>()
        edges1.addEdge(edge1)
        edges1.addEdge(edge2)


        val edges2 = MutableEdges<Language>()

        edges2.addEdge(edge1)
        edges2.addEdge(edge3)

        edges1 -= edges2

        Truth.assertThat(
            edges1.edgesCount()
        ).isEqualTo(1)

        Truth.assertThat(
            edges1.contain(edge1)
        ).isFalse()

        Truth.assertThat(
            edges1.contain(edge2)
        ).isTrue()

        Truth.assertThat(
            edges1.contain(edge3)
        ).isFalse()
    }

    @Test
    fun `minusAssignEdgesOperator when there is not overlapping edges must nothing remove`() {

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

        val edge1 = Edge(start = state1, transition = Language.a, end = state1)
        val edge2 = Edge(start = state1, transition = Language.b, end = state1)
        val edge3 = Edge(start = state1, transition = Language.a, end = state2)

        val edges1 = MutableEdges<Language>()
        edges1.addEdge(edge2)


        val edges2 = MutableEdges<Language>()

        edges2.addEdge(edge1)
        edges2.addEdge(edge3)

        edges1 -= edges2

        Truth.assertThat(
            edges1.edgesCount()
        ).isEqualTo(1)

        Truth.assertThat(
            edges1.contain(edge1)
        ).isFalse()

        Truth.assertThat(
            edges1.contain(edge2)
        ).isTrue()

        Truth.assertThat(
            edges1.contain(edge3)
        ).isFalse()
    }

    @Test
    fun `plusAssignEdgeOperator  when there is no overlapping edges must union all edged`() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val edge1 = Edge(start = state1, transition = Language.a, end = state1)
        val edge2 = Edge(start = state1, transition = Language.b, end = state1)

        val edges1 = MutableEdges<Language>()
        edges1.addEdge(edge1)


        edges1 += edge2

        Truth.assertThat(
            edges1.contain(edge1)
        ).isTrue()

        Truth.assertThat(
            edges1.contain(edge2)
        ).isTrue()
    }

    @Test
    fun `plusAssignEdgeOperator edge when there is overlapping edges must ignore the overlapping edges`() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val edge1 = Edge(start = state1, transition = Language.a, end = state1)

        val edges1 = MutableEdges<Language>()
        edges1.addEdge(edge1)


        assertThrows<DuplicatedEdgeException> {
            edges1 += edge1
        }

        Truth.assertThat(
            edges1.edgesCount()
        ).isEqualTo(1)

        Truth.assertThat(
            edges1.contain(edge1)
        ).isTrue()
    }

    @Test
    fun `minusAssignEdgeOperator edge when there is overlapping edges must just remove overlapping edges `() {

        val stateId1 = 1
        val stateName1 = "s1"
        val isFinalState1 = false
        val state1 = State(
            id = stateId1,
            name = stateName1,
            isFinal = isFinalState1
        )

        val edge1 = Edge(start = state1, transition = Language.a, end = state1)
        val edge2 = Edge(start = state1, transition = Language.b, end = state1)

        val edges1 = MutableEdges<Language>()
        edges1.addEdge(edge1)
        edges1.addEdge(edge2)


        edges1 -= edge2

        Truth.assertThat(
            edges1.edgesCount()
        ).isEqualTo(1)

        Truth.assertThat(
            edges1.contain(edge1)
        ).isTrue()

        Truth.assertThat(
            edges1.contain(edge2)
        ).isFalse()

    }

    @Test
    fun `minusAssignEdgeOperator edge when there is not overlapping edges must nothing remove`() {

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

        val edge1 = Edge(start = state1, transition = Language.a, end = state1)
        val edge2 = Edge(start = state1, transition = Language.b, end = state1)
        val edge3 = Edge(start = state1, transition = Language.a, end = state2)

        val edges1 = MutableEdges<Language>()
        edges1.addEdge(edge2)


        assertThrows<NoSuchEdgeException> {
            edges1 -= edge1
        }

        Truth.assertThat(
            edges1.edgesCount()
        ).isEqualTo(1)

        Truth.assertThat(
            edges1.contain(edge1)
        ).isFalse()

        Truth.assertThat(
            edges1.contain(edge2)
        ).isTrue()

        Truth.assertThat(
            edges1.contain(edge3)
        ).isFalse()
    }

}