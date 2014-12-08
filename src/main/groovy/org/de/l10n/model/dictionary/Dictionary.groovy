package org.de.l10n.model.dictionary

import org.de.l10n.model.dictionary.token.BaseToken
import org.de.l10n.model.dictionary.token.ExpressionToken
import org.de.l10n.model.dictionary.token.PluralToken
import org.de.l10n.model.dictionary.token.ReferenceToken
import org.de.l10n.model.dictionary.token.TextToken

class Dictionary {
    def private Map<String, ?> tokens;
    def Map content;

    def Dictionary(Map<String, ?> content=new java.util.HashMap<String, ?>()) {
        this.content = content;
        this.tokens = buildTokens(content);
    }

    /*
    * given a map of keys, will return flat key structure ok Token arrays.
    * For Example:
    *   {
    *       "nested1":  {
    *           "nested2": "nested2 %{expression1} p('singular || plural)"
    *       },
    *       "foo": "Foo"
    *   }
    *
    *   returns map structure
    *   {
    *       "nested1.nested" = [TextToken, ExpressionToken, PluralToken],
    *       "foo": [TextToken]
    *   }
     */
    def private Map<String, ?> buildTokens(Map<String, ?> content, List<String> prefixes=[]) {

        def tokens = [:];

        content.each {key, value ->
            prefixes << key;

            if (value instanceof java.util.Map) {
                tokens += buildTokens(value, prefixes);
            }
            else {
                def flatKey = prefixes.join(".");
                tokens[flatKey] = Tokenizer.tokenize(value);
                prefixes = [];
            }
        }

        return tokens;
    }

    def void setContent(Map<String, ?> content) {
        this.content = content;
        this.tokens = buildTokens(content);
    }

    def Map<String, ?> getTokens() {
        return tokens;
    }

    def List<BaseToken> getTokens(String term) {

        return (tokens[term] == null ? [] : tokens[term])
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

        /* identifies all possible tokens in a phrase, returning a list of found tokens
        *   For example:
        *   tokenize("See the %{color} fox run")
        *
        *   will return...
        *   [TextToken("See the"), ExpressionToken("%{color}"), TextToken("fox run")]
        */
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

        /*
        * parses out a particular token from a phrase. Any remaining unparsed tokens will be split into separate phrases
        * occurring before the parsed token and after the parsed token respectively.
        * For example:
        *
        * parseMultipleTokens("See the %{color} fox run", TokenRegex.EXPRESSION)
        *
        * will return...
        * beforeMatched = "See the"
        * matched = "%{color}"
        * afterMatched = "fox run"
        *
         */
        def private static List<BaseToken> parseMultipleTokens(String phrase, TokenRegex tr) {

            def matcher = phrase =~ /(.*)(${tr.regex})(.*)/
            def beforeMatched = matcher[0][1] ? matcher[0][1] : ""
            def matched = matcher[0][2]
            def afterMatched = matcher[0][3] ? matcher[0][3] : ""

            def List<BaseToken> tokens = []
            tokens.addAll(tokenize(beforeMatched))
            tokens.addAll(tokenize(matched))
            tokens.addAll(tokenize(afterMatched))

            return tokens
        }
    }

}
