package main.groovy

import main.groovy.Globalization

/**
 * Created by mhatch on 12/5/14.
 */
class Translation {
    def globalization;

    def Translation(Globalization g=new Globalization()) {
        globalization = g;
    }

    def findTranslation(String term, String locale, String version) {

        def criteria = parseTerm(term);
        def dictionary = findDictionary(criteria["dictionaryName"], locale, version);
        def translation = findTerm(dictionary, criteria["dictionaryPath"])

        return translation;
    }

    def private Map findDictionary(String name, String locale, String version) {

        def dictionary = globalization.findDictionary(name, locale, version);

        return dictionary;
    }

    def private Map parseTerm(String term) {

        def tokens = term.tokenize(".");
        def dictionaryName = tokens[0];
        def dictionaryPath = tokens;
        def criteria = ["dictionaryName": dictionaryName, "dictionaryPath": dictionaryPath];

        return criteria;
    }

    def private findTerm(dictionary, paths) {

        def dictionaryValue = dictionary;

        for(term in paths) {
            if(dictionaryValue instanceof java.util.Map && dictionaryValue.containsKey(term)) {
                dictionaryValue = dictionaryValue[term];
            }
            else {
                dictionaryValue = "Missing Translation";
                //TODO: log missing translation
                break;
            }
        }

        return dictionaryValue;
    }
}
