package org.de.l10n.model

import org.de.l10n.service.DictionaryService


class Translator {
    def DictionaryService dictionaryService;

    def findTranslation(String term, String locale, String version) {

        def criteria = parseTerm(term);
        def dictionary = dictionaryService.findDictionary(criteria["dictionaryName"], locale, version);
        def translation = findTerm(dictionary, criteria["dictionaryPath"])

        return translation;
    }

    /*
    * parses a term to identify the dictionary name and the path of the key to translate
    * For Example:
    *   parseTerm("common.terms.applyNow")
    *
    *   will return ["dictionaryName": "common", "dictionaryPath": "common.terms.applyNow"]
     */
    def private Map<String, ?> parseTerm(String term) {

        def tokens = term.tokenize(".");
        def dictionaryName = tokens[0];
        def dictionaryPath = tokens;
        def criteria = ["dictionaryName": dictionaryName, "dictionaryPath": dictionaryPath];

        return criteria;
    }

    def private findTerm(Map<String, ?> dictionary, List<String> paths) {

        def dictionaryValue = dictionary;

        for(String term in paths) {
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
