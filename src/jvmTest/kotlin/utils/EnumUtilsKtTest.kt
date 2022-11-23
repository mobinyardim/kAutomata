package utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import com.google.common.truth.Truth.assertThat

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
}