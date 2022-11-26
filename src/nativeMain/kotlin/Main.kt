import com.mobinyardim.libs.kautomata.NFA
import com.mobinyardim.libs.kautomata.State

fun main() {

    val nfa = NFA<Language>()

    val s1 = nfa.addState(State(1, "S1", false))
    val s2 = nfa.addState(State(2, "S2", false))
    val s3 = nfa.addState(State(3, "S3", true))

    nfa.addEdge(s1, Language.A, s2)
    nfa.addEdge(s2, Language.B, s3)

    println("Hello, Kotlin/Native!")
}

enum class Language {
    A,
    B
}