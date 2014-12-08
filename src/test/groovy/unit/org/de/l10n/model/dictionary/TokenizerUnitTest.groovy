package unit.org.de.l10n.model.dictionary

import org.de.l10n.model.dictionary.token.ExpressionToken
import org.de.l10n.model.dictionary.token.PluralToken
import org.de.l10n.model.dictionary.token.TextToken
import org.junit.Test;


import org.de.l10n.model.dictionary.Dictionary.Tokenizer

class TokenizerUnitTest {

    @Test
    void "tokenize should parse all tokens" () {

        def content = "The cat is %{color} and has p('eye || eyes', n)"
        def tokens = Tokenizer.tokenize(content)
        def expectedTokens = [
                new TextToken("The cat is "),
                new ExpressionToken("%{color}"),
                new TextToken(" and has "),
                new PluralToken("p('eye || eyes', n)"),
                new TextToken("")
        ]

        assert expectedTokens == tokens
    }
}
