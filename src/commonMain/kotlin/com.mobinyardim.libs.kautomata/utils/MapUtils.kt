package com.mobinyardim.libs.kautomata.utils


fun <K, V> MutableMap<K, V>.removeIf(
    filter: (Map.Entry<K, V>) -> Boolean
) {
    for (entry in this) {
        if (filter(entry)) {
            this.remove(entry.key)
        }
    }
}