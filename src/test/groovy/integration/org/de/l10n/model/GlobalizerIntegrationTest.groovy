package integration.org.de.l10n.model

import org.de.l10n.model.Globalizer

import org.junit.Test;
import org.junit.Before;

class GlobalizerIntegrationTest {
    def globalization;

    @Before
    void setUp() {
        globalization = new Globalizer()
    }

    @Test
    void "createDictionary should return empty dictionary when dictionary file not found"() {

        def dictionary = globalization.createDictionary("fakeName", "fakeLocale");

        assert dictionary instanceof java.util.Map == true;
        assert dictionary.size() == 0;
    }

    @Test
    void "createDictionary should return dictionary when file found"() {

        def dictionary = globalization.createDictionary("common", "en-us");

        assert dictionary instanceof java.util.Map == true;
        assert dictionary.size() != 0;
    }
}
