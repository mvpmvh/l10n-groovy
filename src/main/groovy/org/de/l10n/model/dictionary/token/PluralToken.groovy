package org.de.l10n.model.dictionary.token


class PluralToken extends BaseToken {

    def PluralToken(String content) {
        this.content = content
    }

    @Override
    String translate(Map<String, ?> expression) {

        def strippedContent = stripContent()
        def pluralizationOptions = strippedContent.tokenize("||")
        def result = expression?.n != 1 ? pluralizationOptions[1] : pluralizationOptions[0]

        return result.trim()
    }

    @Override
    String getLeftToken() {
        return "^p\\(['\"]"
    }

    @Override
    String getRightToken() {
        return "['\"],\\sn\\)"
    }
}
