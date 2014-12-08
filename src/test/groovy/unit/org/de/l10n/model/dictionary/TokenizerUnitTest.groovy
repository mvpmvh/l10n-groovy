package unit.org.de.l10n.model.dictionary

import org.de.l10n.model.dictionary.token.ExpressionToken
import org.de.l10n.model.dictionary.token.PluralToken
import org.de.l10n.model.dictionary.token.TextToken
import org.junit.Test;


import org.de.l10n.model.dictionary.Dictionary.Tokenizer

class TokenizerUnitTest {

    @Test
    void "tokenize should parse all tokens" () {

        def phrase = "The cat is %{color} and has p('eye || eyes', n)"
        def tokens = Tokenizer.tokenize(phrase)
        def expectedTokens = [
                new TextToken("The cat is "),
                new ExpressionToken("%{color}"),
                new TextToken(" and has "),
                new PluralToken("p('eye || eyes', n)"),
                new TextToken("")
        ]

        assert expectedTokens == tokens
    }

    @Test
    void "tokenize should return plural token when plural grammar applied" () {

        def phrase = "p('height || heights)"
        def tokens = Tokenizer.tokenize(phrase)
        def expectedTokens = [new PluralToken("p('height || heights)")]

        assert expectedTokens == tokens
    }

    @Test
    void "tokenize should return expression token when expression grammar applied" () {

        def phrase = "%{color}"
        def tokens = Tokenizer.tokenize(phrase)
        def expectedTokens = [new ExpressionToken("%{color}")]

        assert expectedTokens == tokens
    }

    @Test
    void "tokenize return text token when plain text grammar applied" () {

        def phrase = "Hello, World!"
        def tokens = Tokenizer.tokenize(phrase)
        def expectedTokens = [new TextToken("Hello, World!")]

        assert expectedTokens == tokens
    }
}
