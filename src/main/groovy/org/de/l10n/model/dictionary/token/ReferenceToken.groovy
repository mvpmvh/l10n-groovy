package org.de.l10n.model.dictionary.token


class ReferenceToken extends BaseToken {

    def ReferenceToken(String content) {
        this.content = content
    }

    @Override
    String translate(Map<String, ?> expression) {
        return null
    }

    @Override
    String getLeftToken() {
        return "^t\\(['\"]"
    }

    @Override
    String getRightToken() {
        return "['\"]\\)"
    }

    def String getPath() {
        return stripContent()
    }
}
