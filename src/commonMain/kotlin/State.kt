data class State(
    val id: Int,
    val name: String,
    val isFinal: Boolean
) {
    override fun hashCode(): Int {
        return id
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}