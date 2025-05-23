object KotlinTags {
    /*
        HTML rendering library
        ScalaTags
            html(
                attr:="something",
                head(
                    title("my web page"),
                    ...
                ),
                body(
                    div(
                        p("this is a paragraph")
                    )
                )

        KotlinTags
     */

    sealed interface HTMLElement

    // add data types for the rest of the HTML tags

    // step 1. define data types for the HTML tags we want to support
    //          - html, head, title, body, div, p

    data class HTML(val head: Head, val body: Body): HTMLElement {
        override fun toString(): String =
            "<html>\n$head\n$body\n</html>"
    }

    data class Head(val title: Title): HTMLElement {
        override fun toString(): String =
            "<head>\n$title\n</head>"
    }

    data class Title(val content: String): HTMLElement {
        override fun toString(): String =
            "<title>$content</title>"
    }

    data class Body(val children: List<HTMLElement>): HTMLElement {
        override fun toString(): String =
            children.joinToString("\n", "<body>", "</body>")
    }

    data class Div(val children: List<HTMLElement>, val id: String? = null, val className: String? = null): HTMLElement {
        override fun toString(): String {
            val idAttr    = id?.let        { " id=\"$it\""    } ?: ""
            val classAttr = className?.let { " class=\"$it\"" } ?: ""
            val innerHTML = children.joinToString("\n")
            return "<div$idAttr$classAttr>$innerHTML</div>"
        }
    }

    data class P(val content: String) : HTMLElement {
        override fun toString(): String = "<p>$content</p>"
    }
}