package org.de.l10n.model.dictionary.token


class ExpressionToken extends BaseToken {

    def ExpressionToken(String content) {
        this.content = content
    }

    @Override
    def String translate(Map<String, ?> expression) {

        def strippedContent = stripContent()
        def result = expression[strippedContent] ? expression[strippedContent] : ""

        return result
    }

    @Override
    def String getLeftToken() {
        return "^%\\{"
    }

    @Override
    def String getRightToken() {
        return "}\$"
    }
}
