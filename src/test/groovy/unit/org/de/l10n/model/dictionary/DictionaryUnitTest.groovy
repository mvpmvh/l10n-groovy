package unit.org.de.l10n.model.dictionary

import groovy.json.JsonSlurper
import org.junit.Test;
import org.junit.Before;

import org.de.l10n.model.dictionary.Dictionary

class DictionaryUnitTest {
    def Dictionary dictionary;
    def String testJSON =
        '''
            {
                "nested1": {
                    "nested2": {
                        "nested3" : "nested3"
                    }
                 },
                 "foo": "Foo"
             }
        ''';

    def testContent = new JsonSlurper().parseText(testJSON);

    @Before
    void setUp() {
        dictionary = new Dictionary();
    }

    @Test
    void "default dictionary should have empty terms" () {

        assert dictionary.getTerms() instanceof java.util.Map;
        assert dictionary.getTerms().size() == 0;
    }

    @Test
    void "dictionary should build terms when content set" () {

        dictionary.setContent(testContent);

        assert dictionary.getTerms() instanceof java.util.Map;
        assert dictionary.getTerms().size() > 0;
    }

    @Test
    void "buildTerms should return flat keys of content" () {

        def flatDictionary = dictionary.buildTerms(testContent);
        def expectedDictionary = ["nested1.nested2.nested3": "nested3", "foo": "Foo"];

        assert expectedDictionary == flatDictionary
    }
}
