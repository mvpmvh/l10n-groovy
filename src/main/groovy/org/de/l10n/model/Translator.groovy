package org.de.l10n.model

import org.de.l10n.model.dictionary.token.BaseToken
import org.de.l10n.model.dictionary.token.ReferenceToken
import org.de.l10n.service.DictionaryService


class Translator {
    def DictionaryService dictionaryService

    def Translator() {
        dictionaryService = new DictionaryService()
    }

    def findTranslation(String term, String locale, String version, Map<String, ?> expression=new java.util.HashMap()) {

        def criteria = parseTerm(term)
        def dictionary = dictionaryService.findDictionary(criteria["dictionaryName"], locale, version)
        def tokens = dictionary.getTokens(term)
        def translation = translateTokens(tokens, expression, locale, version)

        if(translation == "") {
            translation = "Missing Translation"
        }

        return translation
    }

    def Map<String, ?> findDictionaryContent(String dictionaryName, String locale, String version) {
        def dictionary = dictionaryService.findDictionary(dictionaryName, locale, version)
        def content = dictionary.getContent()

        return content
    }

    /*
    * parses a term to identify the dictionary name and the path of the key to translate
    * For Example:
    *   parseTerm("common.terms.applyNow")
    *
    *   will return ["dictionaryName": "common", "dictionaryPath": "common.terms.applyNow"]
     */
    def private Map<String, String> parseTerm(String term) {

        def tokens = term.tokenize(".");
        def dictionaryName = tokens[0];
        def dictionaryPath = tokens;
        def criteria = ["dictionaryName": dictionaryName, "dictionaryPath": dictionaryPath];

        return criteria;
    }

    /*
    *   builds a translated string based upon to result of each token.translate()
    */
    def private String translateTokens(List<BaseToken> tokens, Map<String, ?> expression, String locale, String version) {

        def translation = new StringBuilder("")

        tokens.each {token ->
            if(token instanceof ReferenceToken) {
                translation.append(findTranslation(token.getPath(), locale, version, expression))
            }
            else {
                translation.append(token.translate(expression))
            }
        }

        return translation.toString()
    }
}
