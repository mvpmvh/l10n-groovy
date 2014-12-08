package org.de.l10n.model

import org.de.l10n.model.dictionary.token.BaseToken
import org.de.l10n.model.dictionary.token.ReferenceToken
import org.de.l10n.service.DictionaryService


class Translator {
    def DictionaryService dictionaryService

    def findTranslation(String term, String locale, String version, Map<String, ?> expression=new java.util.HashMap<String, ?>()) {

        def criteria = parseTerm(term)
        def dictionary = dictionaryService.findDictionary(criteria["dictionaryName"], locale, version)
        def tokens = dictionary.getTokens(term)
        def translation = translateTokens(tokens, expression, locale, version)

        if(translation == "") {
            translation = "Missing Translation"
        }

        return translation
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

    def private String translateTokens(List<BaseToken> tokens, Map<String, ?> expression, String locale, String version) {

        def translation = ""

        tokens.each {token ->
            if(token instanceof ReferenceToken) {
                translation += findTranslation(token.getPath(), locale, version, expression)
            }
            else {
                translation += token.translate(expression)
            }
        }

        return translation
    }
}
