package integration

import org.de.l10n.Globalizer
import org.de.l10n.Translator;
import org.junit.Test;
import org.junit.Before;

class TranslatorIntegrationTest {
    def translation

    @Before
    void setUp() {
        translation = new Translator(new Globalizer());

    }

    @Test
    void "findTranslation should return the translation for existing key in dictionary"() {

        def t = translation.findTranslation("common.terms.applyNow", "en-us", "fooVersion");

        assert t == "Apply Now";
    }

    @Test
    void "findTranslation should return dictionary section for key that is dictionary value" () {

        def t = translation.findTranslation("common.terms", "en-us", "fooVersion");

        assert t instanceof java.util.Map;
    }

    @Test
    void "findTranslation should return default locale translation when translation not found for non-default locale" () {

        def t = translation.findTranslation("common.terms.applyNow", "unknown-locale", "fooVersion");

        assert t == "Apply Now";
    }

    @Test
    void "findTranslation should return 'Missing Translation' for missing key in dictionary" () {

        def t = translation.findTranslation("fake.path.to.term", "en-us", "fooVersion");

        assert t == "Missing Translation";
    }

}