package org.de.l10n.model.dictionary

import org.de.l10n.model.dictionary.token.BaseToken
import org.de.l10n.model.dictionary.token.ExpressionToken
import org.de.l10n.model.dictionary.token.PluralToken
import org.de.l10n.model.dictionary.token.ReferenceToken

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

        def static List<BaseToken> tokenize(String content) {

            def tokens = []

            if(isReferenceGrammar(content)) {
                tokens << new ReferenceToken(content)
            }
            else if(isPluralGrammar(content)) {
                tokens << new PluralToken(content)
            }
            else if(isExpressionGrammar(content)) {
                tokens << new ExpressionToken(content)
            }
            else if(hasReferenceGrammar(content)) {
                tokens = parseMultipleTokens(content, TokenRegex.REFERENCE)
            }

            return tokens
        }

        def private static boolean isReferenceGrammar(String content) {
            return content ==~ /^t\(.*\)$/
        }

        def private static boolean isPluralGrammar(String content) {
            return content ==~ /^p\(.*\)$/
        }

        def private static boolean isExpressionGrammar(String content) {
            return content ==~ /^%\{.*\}$/
        }

        def private static boolean hasReferenceGrammar(String content) {
            return content ==~ /t\(.*\)/
        }

        def private static List<BaseToken> parseMultipleTokens(String content, TokenRegex tr) {

            def matcher = content =~ /(.*)(${tr.regex})(.*)/
            def before = matcher[0][1] ? matcher[0][1] : ""
            def middle = matcher[0][2]
            def end = matcher[0][3] ? matcher[0][3] : ""

            def List<BaseToken> tokens = []
            tokens.addAll(tokenize(before))
            tokens.addAll(tokenize(middle))
            tokens.addAll(tokenize(end))

            return tokens
        }
    }

}
