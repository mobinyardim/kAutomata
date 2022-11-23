import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat
import exceptions.DuplicatedStateException
import org.junit.jupiter.api.assertThrows

internal class AutomataTest {

    enum class Language {
        A,
        B
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
    fun `containsEdge must return true when edge added to edges field`() {
        //TODO
    }

    @Test
    fun `addEdge when edges empty must correctly add edge`() {
        val automata = object : Automata<Language>() {}

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

        val transition = Language.A
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
        val automata = object : Automata<Language>() {}

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

        val transition1 = Language.A
        val transition2 = Language.B

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

}