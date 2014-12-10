package org.de.l10n.model.dictionary.token


class DateToken extends BaseToken {

    def DateToken(String content) {
        this.content = content
    }

    @Override
    String translate(Map<String, ?> expression) {

        def format = stripContent()
        def Date d = new Date(expression["date"])
        def String newDate = d.format(format)

        return newDate
    }

    @Override
    String getLeftToken() {
        return "^d\\(['\"]"
    }

    @Override
    String getRightToken() {
        return "['\"]\\)"
    }

}
