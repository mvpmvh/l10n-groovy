package integration.org.de.l10n.model


import org.de.l10n.model.Translator
import org.junit.Test
import org.junit.Before

class TranslatorIntegrationTest {
    def Translator translator

    @Before
    void setUp() {
        translator = new Translator()
    }

    @Test
    void "findTranslation should return the translation for existing key in dictionary"() {

        assert translator.findTranslation("common.terms.applyNow", "en-us", "fooVersion") == "Apply Now"
    }

    @Test
    void "findTranslation should not return dictionary section for partial key in dictionary" () {

        assert translator.findTranslation("common.terms", "en-us", "fooVersion") == "Missing Translation"
    }

    @Test
    void "findTranslation should return default locale translation when translation not found for non-default locale" () {

        assert translator.findTranslation("common.terms.applyNow", "unknown-locale", "fooVersion") == "Apply Now"
    }

    @Test
    void "findTranslation should return 'Missing Translation' for missing key in dictionary" () {

        assert translator.findTranslation("fake.path.to.term", "en-us", "fooVersion") == "Missing Translation";
    }

}