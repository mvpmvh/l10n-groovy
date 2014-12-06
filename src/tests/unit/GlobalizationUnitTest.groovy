package unit

import main.Globalization

/**
 * Created by mhatch on 12/5/14.
 */
import org.junit.Test;
import org.junit.Before;

class GlobalizationUnitTest {
    def globalization;

    @Before
    void setUp() {
        globalization = new Globalization()
    }

    @Test
    void "appendDictionary should add missing keys from one dictionary to another"() {

        def d1 = ["a": "A", "b" : "B"];
        def d2 = ["c": "C"]

        globalization.appendDictionary(d1, d2);

        assert d1["a"] == "A";
        assert d1["b"] == "B";
        assert d1["c"] == "C";
    }

    @Test
    void "appendDictionary should add missing nested keys from one dictionary to another"() {

        def d1 = ["1": "1", "2": ["2.1": "2.1"]];
        def d2 = ["2": ["2.2": "2.2"]];

        globalization.appendDictionary(d1, d2);

        assert d1["1"] == "1";
        assert d1["2"]["2.1"] == "2.1";
        assert d1["2"]["2.2"] == "2.2";
    }

    @Test
    void "appendDictionary should NOT add keys from one dictionary to another when key exists"() {

        def d1 = ["1": "1"];
        def d2 = ["1": "2"];

        globalization.appendDictionary(d1, d2);

        assert d1["1"] == "1";
    }
}
