package unit.org.de.l10n.model.dictionary.token

import org.de.l10n.model.dictionary.token.ExpressionToken
import org.de.l10n.model.dictionary.token.TextToken
import org.junit.Test;


class TokenUnitTest {

    @Test
    void "TexToken should return expected text" () {

        def token = new TextToken("plain text")
        def expression = token.translate()

        assert expression == "plain text"
    }

    @Test
    void "ExpressionToken should return expression variable" () {

        def token = new ExpressionToken("%{color}")
        def expression = ["color": "blue"]

        assert token.translate(expression) == "blue"
    }
}
