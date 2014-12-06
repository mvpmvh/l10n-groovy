package integration

import main.Globalization

/**
 * Created by mhatch on 12/5/14.
 */
import org.junit.Test;
import org.junit.Before;

class GlobalizationIntegrationTest {
    def globalization;

    @Before
    void setUp() {
        globalization = new Globalization(localeDirectory: "src/tests/fixtures/locales")
    }

    @Test
    void "createDictionary should return empty dictionary when file not found"() {

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
