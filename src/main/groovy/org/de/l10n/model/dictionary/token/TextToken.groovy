package org.de.l10n.model.dictionary.token

class TextToken extends BaseToken {

    def TextToken(String content) {
        this.content = content
    }

    @Override
    def String translate(Map<String, ?> expression) {
        return content
    }

    @Override
    String getLeftToken() {
        return ""
    }

    @Override
    String getRightToken() {
        return ""
    }
}
