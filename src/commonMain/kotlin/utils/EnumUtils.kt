package utils


fun <T : Enum<T>> List<T>.toText(): String {
    return buildString {
        this@toText.forEach {
            append(it.name)
        }
    }
}

inline fun <reified T : Enum<T>> String.toEnumList(): List<T> {
    val list = mutableListOf<T>()
    this.forEach {
        list.add(enumValueOf(it.toString()))
    }
    return list
}