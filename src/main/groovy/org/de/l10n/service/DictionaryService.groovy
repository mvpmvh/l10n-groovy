package org.de.l10n.service

import org.de.l10n.model.dictionary.Dictionary
import static org.de.l10n.model.Globalizer.defaultLocale
import static org.de.l10n.model.Globalizer.localeDirectory

import groovy.json.JsonSlurper

class DictionaryService {
    def JsonSlurper slurper = new JsonSlurper()

    /*
    * returns a @org.de.l10n.model.dictionary.Dictionary by name and locale.
    * If locale is not the default locale, the default locale dictionary content will be appended to requested
    * Dictionary
     */
    def Dictionary findDictionary(String name, String locale=defaultLocale, String version) {

        def dictionaryContent = findDictionaryContent(name, locale)

        if (locale != defaultLocale) {
            def defaultDictionaryContent = findDictionaryContent(name, defaultLocale)
            appendDictionaryContent(dictionaryContent, defaultDictionaryContent)
        }

        return new Dictionary(dictionaryContent)
    }

    /*
    * looks for json dictionary file based upon name and locale.
    * if json dictionary file does not exist, return empty map;
     */
    def private Map<String, ?> findDictionaryContent(String name, String locale) {

        def dictionaryContent = [:]
        def final InputStream inputStream = getClass().getResourceAsStream("${localeDirectory}/${name}/${locale}.json");

        if (inputStream != null) {
            dictionaryContent = slurper.parse(inputStream)
        }

        return dictionaryContent;
    }

    /*
    * merges the contents of dictionary2 into dictionary1 if not already present in dictionary1
    * For Example:
    *   d1 = ["a": "A"]
    *   d2 = ["a": "AA", "b", "B"]
    *   appendDictionaryContent(d1, d2)
    *
    *   will return ["a": "A", "b": "B"]
    *
     */
    def private void appendDictionaryContent(Map<String, ?> dictionary1, Map<String, ?> dictionary2) {

        dictionary2.each { key, value ->
            if(dictionary1.containsKey(key) == false) {
                dictionary1[key] = value
            }
            else if(dictionary1[key] instanceof java.util.Map && value instanceof java.util.Map) {
                appendDictionaryContent(dictionary1[key], value)
            }
        }
    }
}
