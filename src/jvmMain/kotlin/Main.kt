import com.mobinyardim.libs.kautomata.*
import com.mobinyardim.libs.kautomata.utils.toEnumList
import com.mobinyardim.libs.kautomata.utils.toText

fun main() {

    val s1 = State(1, "S1", false)

    val nfa = NFA<Language>(s1)

    val s2 = nfa.addState(State(2, "S2", false))
    val s3 = nfa.addState(State(3, "S3", false))
    val s4 = nfa.addState(State(4, "S4", true))
    val s5 = nfa.addState(State(5, "S5", false))
    val s6 = nfa.addState(State(6, "S6", false))

    nfa.addEdge(s1, null, s2)
    nfa.addEdge(s1, null, s5)
    nfa.addEdge(s2, Language.a, s3)
    nfa.addEdge(s3, Language.a, s4)
    nfa.addEdge(s5, Language.b, s6)
    nfa.addEdge(s6, Language.b, s4)

    nfa.trace(
        string = "aa".toEnumList(),
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

            override fun onTransition(start: State, transition: Language?, endState: State) {
                println("onTransition: from:$start with:${transition?.name ?: "lambda"} to:${endState}")
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