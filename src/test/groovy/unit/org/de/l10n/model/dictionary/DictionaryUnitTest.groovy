package unit.org.de.l10n.model.dictionary

import groovy.json.JsonSlurper
import org.de.l10n.model.dictionary.token.TextToken
import org.junit.Test
import org.junit.Before

import org.de.l10n.model.dictionary.Dictionary

class DictionaryUnitTest {
    def Dictionary dictionary
    def String testJSON =
        '''
            {
                "nested1": {
                    "nested2": {
                        "nested3": "nested3",
                        "nested4": "nested4"
                    },
                    "nested5": "nested5"
                 },
                 "foo": "Foo"
             }
        '''

    def testContent = new JsonSlurper().parseText(testJSON)

    @Before
    void setUp() {
        dictionary = new Dictionary()
    }

    @Test
    void "empty dictionary should have empty tokens" () {

        assert dictionary.getTokens() instanceof java.util.Map
        assert dictionary.getTokens().size() == 0
    }

    @Test
    void "dictionary should build tokens when content set" () {

        dictionary.setContent(testContent)

        assert dictionary.getTokens() instanceof java.util.Map
        assert dictionary.getTokens().size() > 0
    }

    @Test
    void "buildTokens should return flat keys of tokens" () {

        def flatDictionary = dictionary.buildTokens(testContent);
        def expectedDictionary = [
                "nested1.nested2.nested3": [new TextToken("nested3")],
                "nested1.nested2.nested4": [new TextToken("nested4")],
                "nested1.nested5": [new TextToken("nested5")],
                "foo": [new TextToken("Foo")]
        ]

        assert expectedDictionary == flatDictionary
    }
}
