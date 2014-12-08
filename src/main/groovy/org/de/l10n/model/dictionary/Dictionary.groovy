package org.de.l10n.model.dictionary

class Dictionary {
    def private Map<String, ?> terms;
    def Map content;

    def Dictionary(Map<String, ?> content=new java.util.HashMap<String, ?>()) {
        this.content = content;
        this.terms = buildTerms(content);
    }

    /*
    * given a map of keys, will return flat key structure.
    * For Example:
    *   {
    *       "nested1":  {
    *           "nested2": "nested2"
    *       },
    *       "foo": "Foo"
    *   }
    *
    *   returns map structure
    *   {
    *       "nested1.nested" = "nested2",
    *       "foo": "Foo"
    *   }
     */
    def private Map<String, ?> buildTerms(Map<String, ?> content, List<String> prefixes=[]) {

        def terms = [:];

        content.each {key, value ->
            prefixes << key;

            if (value instanceof java.util.Map) {
                terms += buildTerms(value, prefixes);
            }
            else {
                def flatKey = prefixes.join(".");
                terms[flatKey] = value;
                prefixes = [];
            }
        }

        return terms;
    }

    def void setContent(Map<String, ?> content) {
        this.content = content;
        this.terms = buildTerms(content);
    }

    def Map<String, ?> getTerms() {
        return terms;
    }

}
