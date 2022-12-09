package com.mobinyardim.libs.kautomata.utils


fun <K, V> MutableMap<K, V>.removeIf(
    filter: (Map.Entry<K, V>) -> Boolean
) {
    val removedList = mutableListOf<K>()
    for (entry in this) {
        if (filter(entry)) {
            removedList.add(entry.key)
        }
    }
    removedList.forEach {
        this.remove(it)
    }
}