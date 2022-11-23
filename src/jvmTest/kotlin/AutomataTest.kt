import org.junit.jupiter.api.Test
import com.google.common.truth.Truth.assertThat

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
        val s1 = automata.addState(
            state = State(
                id = stateId, name = stateName, isFinal = isFinalState
            )
        )

        assertThat(s1).isEqualTo(state)
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
        automata.addState(
            state = State(
                id = stateId, name = stateName, isFinal = isFinalState
            )
        )

        assertThat(automata.getState(stateId)).isEqualTo(state)
    }
}