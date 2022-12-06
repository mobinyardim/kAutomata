package com.mobinyardim.libs.kautomata.utils

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class MapUtilsKtTest {

    @Test
    fun `removeIf when there is one predicted element must remove predicted element correctly`() {
        val key1 = "moz"
        val value1 = "moz"

        val key2 = "ananas"
        val value2 = "ananas"

        val map = mutableMapOf(key1 to value1, key2 to value2)
        map.removeIf {
            it.key == key1
        }

        assertThat(
            map.containsKey(key2)
        ).isTrue()

        assertThat(
            map.containsKey(key1)
        ).isFalse()

    }

    @Test
    fun `removeIf when there is some predicted element must remove predicted elements correctly`() {
        val key1 = "moz"
        val value1 = "moz"

        val key2 = "ananas"
        val value2 = "ananas"

        val key3 = "xiyar"
        val value3 = "xiyar"

        val map = mutableMapOf(key1 to value1, key2 to value2, key3 to value3)
        map.removeIf {
            it.key == key1 ||
                    it.key == key2
        }

        assertThat(
            map.containsKey(key1)
        ).isFalse()

        assertThat(
            map.containsKey(key2)
        ).isFalse()

        assertThat(
            map.containsKey(key3)
        ).isTrue()
    }
}