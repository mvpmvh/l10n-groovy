package org.de.l10n


class Translator {
    def Globalizer globalization;

    def Translator(Globalizer g=new Globalizer()) {
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
