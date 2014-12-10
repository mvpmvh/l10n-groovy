package integration.org.de.l10n.model

import org.de.l10n.service.DictionaryService

import org.junit.Test
import org.junit.Before

class DictionaryServiceIntegrationTest {
    def dictionaryService

    @Before
    void setUp() {
        dictionaryService = new DictionaryService()
    }

    @Test
    void "findDictionary should return empty dictionary when dictionary file not found"() {

        def dictionary = dictionaryService.findDictionary("fakeName", "fakeLocale", "fooVersion")

        assert dictionary instanceof org.de.l10n.model.dictionary.Dictionary == true
        assert dictionary.isEmpty() == true
    }

    @Test
    void "findDictionary should return dictionary when file found"() {

        def dictionary = dictionaryService.findDictionary("common", "en-us")

        assert dictionary instanceof org.de.l10n.model.dictionary.Dictionary == true
        assert dictionary.isEmpty() == false
    }
}
