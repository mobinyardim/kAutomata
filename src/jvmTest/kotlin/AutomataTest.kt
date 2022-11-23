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
        automata.addState(state =state)

        assertThat(automata.getState(stateId)).isEqualTo(state)
    }
}