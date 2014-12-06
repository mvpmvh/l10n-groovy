package main

import groovy.json.JsonSlurper

/**
 * Created by mhatch on 12/5/14.
 */
class Globalization {
    String localeDirectory = "/locales";
    String defaultLocale = "en-us";

    /*
    * finds a dictionary and will append the default locale dictionary if non-default locale dictionary requested.
     */
    def Map findDictionary(String name, String locale, String version) {

        def dictionary = createDictionary(name, locale);

        if(locale != defaultLocale) {
           def defaultDictionary = createDictionary(name, defaultLocale);
           appendDictionary(dictionary, defaultDictionary);
        }

        return dictionary;
    }

    /*
    * looks for json dictionary file based upon name and locale.
    * if json dictionary file does not exist, return empty map;
     */
    def Map createDictionary(String name, String locale) {

        def dictionary = [:];
        def dictionaryFile = new File("${localeDirectory}/${name}/${locale}.json");

        if(dictionaryFile.exists()) {
            def slurper = new JsonSlurper();
            dictionary = slurper.parse(dictionaryFile);
        }

        return dictionary;
    }

    /*
    * merges the contents of dictionary2 into dictionary1 if not already present in dictionary1
     */
    def private void appendDictionary(Map dictionary1, Map dictionary2) {

        dictionary2.each { key, value ->
            if(dictionary1.containsKey(key) == false) {
                dictionary1[key] = value;
            }
            else if(dictionary1[key] instanceof java.util.Map && value instanceof java.util.Map) {
                appendDictionary(dictionary1[key], value);
            }
        }
    }
}
