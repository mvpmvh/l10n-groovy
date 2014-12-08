package org.de.l10n.model.dictionary

import org.de.l10n.model.dictionary.token.BaseToken
import org.de.l10n.model.dictionary.token.ExpressionToken
import org.de.l10n.model.dictionary.token.PluralToken
import org.de.l10n.model.dictionary.token.ReferenceToken
import org.de.l10n.model.dictionary.token.TextToken

class Dictionary {
    def private Map<String, ?> terms;
    def Map content;

    def Dictionary(Map<String, ?> content=new java.util.HashMap<String, ?>()) {
        this.content = content;
        this.terms = buildTokens(content);
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
    def private Map<String, ?> buildTokens(Map<String, ?> content, List<String> prefixes=[]) {

        def terms = [:];

        content.each {key, value ->
            prefixes << key;

            if (value instanceof java.util.Map) {
                terms += buildTokens(value, prefixes);
            }
            else {
                def flatKey = prefixes.join(".");
                terms[flatKey] = Tokenizer.tokenize(value);
                prefixes = [];
            }
        }

        return terms;
    }

    def void setContent(Map<String, ?> content) {
        this.content = content;
        this.terms = buildTokens(content);
    }

    def Map<String, ?> getTerms() {
        return terms;
    }

    def private static class Tokenizer {
        def private static final enum TokenRegex  {
            REFERENCE("t\\(.+\\)"),
            PLURAL("p\\(.+\\)"),
            EXPRESSION("%\\{.+}")

            def private final String regex

            TokenRegex(String regex) {
                this.regex = regex
            }
        }

        def static List<BaseToken> tokenize(String phrase) {

            def tokens = []

            if(isReferenceGrammar(phrase)) {
                tokens << new ReferenceToken(phrase)
            }
            else if(isPluralGrammar(phrase)) {
                tokens << new PluralToken(phrase)
            }
            else if(isExpressionGrammar(phrase)) {
                tokens << new ExpressionToken(phrase)
            }
            else if(hasReferenceGrammar(phrase)) {
                tokens = parseMultipleTokens(phrase, TokenRegex.REFERENCE)
            }
            else if(hasPluralGrammar(phrase)) {
                tokens = parseMultipleTokens(phrase, TokenRegex.PLURAL)
            }
            else if(hasExpressionGrammar(phrase)) {
                tokens = parseMultipleTokens(phrase, TokenRegex.EXPRESSION)
            }
            else {
                tokens << new TextToken(phrase)
            }

            return tokens
        }

        def private static boolean isReferenceGrammar(String phrase) {
            return phrase ==~ /^t\(.*\)$/
        }

        def private static boolean isPluralGrammar(String phrase) {
            return phrase ==~ /^p\(.*\)$/
        }

        def private static boolean isExpressionGrammar(String phrase) {
            return phrase ==~ /^%\{.*\}$/
        }

        def private static boolean hasReferenceGrammar(String phrase) {
            return phrase ==~ /.*t\(.*\).*/
        }

        def private static boolean hasPluralGrammar(String phrase) {
            return phrase ==~ /.*p\(.*\).*/
        }

        def private static boolean hasExpressionGrammar(String phrase) {
            return phrase ==~ /.*%\{.*\}.*/
        }

        def private static List<BaseToken> parseMultipleTokens(String phrase, TokenRegex tr) {

            def matcher = phrase =~ /(.*)(${tr.regex})(.*)/
            def start = matcher[0][1] ? matcher[0][1] : ""
            def middle = matcher[0][2]
            def end = matcher[0][3] ? matcher[0][3] : ""

            def List<BaseToken> tokens = []
            tokens.addAll(tokenize(start))
            tokens.addAll(tokenize(middle))
            tokens.addAll(tokenize(end))

            return tokens
        }
    }

}
