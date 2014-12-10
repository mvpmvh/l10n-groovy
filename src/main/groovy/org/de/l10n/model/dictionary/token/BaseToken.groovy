package org.de.l10n.model.dictionary.token

abstract class BaseToken {
    def protected String content

    def protected String stripContent() {
        return content.replaceFirst(/${leftToken}/, "").replaceFirst(/${rightToken}/, "")
    }

    def abstract String translate(Map<String, ?> expression)

    def abstract String getLeftToken()

    def abstract String getRightToken()

    def boolean equals(BaseToken otherToken) {

        return this.class == otherToken.class && this.content == otherToken.content
    }

}
