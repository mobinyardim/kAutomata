import utils.toEnumList
import utils.toText

fun main() {

    val s1 = State(1, "S1", false)

    val nfa = NFA<Language>(s1)

    val s2 = nfa.addState(State(2, "S2", false))
    val s3 = nfa.addState(State(3, "S3", true))

    nfa.addEdge(s1, Language.a, s2)
    nfa.addEdge(s2, Language.a, s2)
    nfa.addEdge(s2, Language.a, s3)

    nfa.trace(
        string = "aab".toEnumList(),
        automataStateTracer = object : AutomataStateTracer<Language> {
            override fun onStart() {
                println("onStart")
            }

            override fun onCurrentStateChange(state: State) {
                println("onCurrentStateChange:$state")
            }

            override fun onFinalState(state: State) {
                println("onFinalState:$state")
            }

            override fun onTrap(state: State, notConsumedString: List<Language>) {
                println("onTrap:$state notConsumedString:${notConsumedString.toText()}")
            }

        }
    )
}

enum class Language {
    a,
    b
}