package com.mobinyardim.libs.kautomata

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.assertThrows
import com.google.common.truth.Truth.assertThat
import com.mobinyardim.libs.kautomata.utils.toEnumList
import com.mobinyardim.libs.kautomata.utils.toText

internal class EnumUtilsKtTest {


    enum class Language {
        a,
        b
    }

    @Test
    fun `toText when list is not empty`() {
        val listOfSymbols = listOf(
            Language.a, Language.b, Language.b
        )

        assertThat(listOfSymbols.toText()).isEqualTo("abb")
    }

    @Test
    fun `toText when list is empty`() {
        val listOfSymbols = listOf<Language>()

        assertThat(listOfSymbols.toText()).isEqualTo("")
    }

    @Test
    fun `toEnumList when text is empty`() {
        val text = ""

        assertThat(text.toEnumList<Language>()).isEqualTo(listOf<Language>())
    }

    @Test
    fun `toEnumList when text contain illegal character`() {
        val text = "x"

        assertThrows<IllegalArgumentException> {
            text.toEnumList<Language>()
        }
    }

    @Test
    fun `toEnumList when text is valid`() {
        val text = "abab"

        assertThat(text.toEnumList<Language>()).isEqualTo(
            listOf(Language.a, Language.b, Language.a, Language.b)
        )
    }
}