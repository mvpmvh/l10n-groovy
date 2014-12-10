package unit.org.de.l10n.model.dictionary.token

import org.de.l10n.model.dictionary.token.DateToken
import org.de.l10n.model.dictionary.token.ExpressionToken
import org.de.l10n.model.dictionary.token.PluralToken
import org.de.l10n.model.dictionary.token.ReferenceToken
import org.de.l10n.model.dictionary.token.TextToken
import org.junit.Test


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

    @Test
    void "PluralToken should handle single-qoute string with plural" () {

        def token = new PluralToken('name || names')
        def expression = ["n": 1]

        assert token.translate(expression) == "name"
    }

    @Test
    void "PluralToken should handle double-qoute string with plural"() {

        def token = new PluralToken("name || names")
        def expression = ["n": 1]

        assert token.translate(expression) == "name"
    }

    @Test
    void "PluralToken should handle string in nestedPlural form" () {

        def token = new PluralToken("p('name || names', n)")
        def expression = ["n": 1]

        assert token.translate(expression) == "name"
    }

    @Test
    void "PluralToken should return plural form"() {

        def token = new PluralToken("p('name || names', n)")
        def expression = ["n": 5]

        assert token.translate(expression) == "names"
    }

    @Test
    void "ReferenceToken should return its path" () {

        def token = new ReferenceToken("t('path.to.term')")

        assert token.getPath() == "path.to.term"
    }

    @Test
    void "DateToken should return formatted date" () {

        def token = new DateToken("d('MM-dd-yyyy')")
        def expression = ["date": "12/04/2014"]

        assert token.translate(expression) == "12-04-2014"
    }
}
