package unit.org.de.l10n.service

import org.de.l10n.service.DictionaryService
import org.junit.Before
import org.junit.Test

class DictionaryServiceUnitTest {
    def DictionaryService dictionaryService

    @Before
    void setUp() {
        dictionaryService = new DictionaryService()
    }

    @Test
    void "appendDictionaryContent should add missing keys from one dictionary content to another"() {

        def d1 = ["a": "A", "b": "B"]
        def d2 = ["c": "C"]

        dictionaryService.appendDictionaryContent(d1, d2)

        assert d1["a"] == "A"
        assert d1["b"] == "B"
        assert d1["c"] == "C"
    }

    @Test
    void "appendDictionaryContent should add missing nested keys from one dictionary content to another"() {

        def d1 = ["1": "1", "2": ["2.1": "2.1"]]
        def d2 = ["2": ["2.2": "2.2"]]

        dictionaryService.appendDictionaryContent(d1, d2)

        assert d1["1"] == "1"
        assert d1["2"]["2.1"] == "2.1"
        assert d1["2"]["2.2"] == "2.2"
    }

    @Test
    void "appendDictionaryContent should NOT add keys from one dictionary content to another when key exists"() {

        def d1 = ["1": "1"]
        def d2 = ["1": "2"]

        dictionaryService.appendDictionaryContent(d1, d2)

        assert d1["1"] == "1"
    }
}
