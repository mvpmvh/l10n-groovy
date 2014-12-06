package integration


import main.groovy.Globalization;
import main.groovy.Translation;


import org.junit.Test;
import org.junit.Before;

class TranslationIntegrationTest {
    def translation, globalization;

    @Before
    void setUp() {
        globalization = new Globalization(localeDirectory: "src/test/resources/fixtures/locales");
        translation = new Translation(globalization);

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