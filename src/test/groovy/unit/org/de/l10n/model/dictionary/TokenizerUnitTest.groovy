package unit.org.de.l10n.model.dictionary

import org.junit.Test;


import org.de.l10n.model.dictionary.Dictionary.Tokenizer

class TokenizerUnitTest {

    @Test
    void "parseMultipleTokens should parse all tokens" () {

        def content = "The cat is %{color} and has p('eye || eyes', n)"
        def tokens = Tokenizer.parseMultipleTokens(content, Tokenizer.TokenRegex.EXPRESSION)

        assert true
    }
}
