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

    // add builders for the rest of the tags: HTMLBuilder, HeadBuilder, BodyBuilder

    // step 2. define some "builders" that enable the DSL for every tag we want to support
    //          - HTMLBuilder, HeadBuilder, BodyBuilder, DivBuilder
    class HTMLBuilder {
        private lateinit var head: Head
        private lateinit var body: Body

        fun head(init: HeadBuilder.() -> Unit) {
            val builder = HeadBuilder()
            builder.init()
            head = builder.build()
        }

        fun body(init: BodyBuilder.() -> Unit) {
            val builder = BodyBuilder()
            builder.init()
            body = builder.build()
        }

        fun build(): HTML =
            HTML(head, body)
    }

    class HeadBuilder {
        private lateinit var title: Title

        fun title(content: String) {
            title = Title(content)
        }

        fun build(): Head = Head(title)
    }

    class BodyBuilder {
        private val children = mutableListOf<HTMLElement>()

        fun div(id: String? = null, className: String? = null, init: DivBuilder.() -> Unit) {
            val builder = DivBuilder(id, className)
            builder.init()
            children.add(builder.build())
        }

        fun p(content: String) {
            children.add(P(content))
        }

        fun build() =
            Body(children)
    }

    class DivBuilder(val id: String?, val className: String?) {
        private val children = mutableListOf<HTMLElement>()
        fun p(content: String) {
            children.add(P(content))
        }

        // expose a "build" method to give me back the final data structure
        fun build() = Div(children, id, className)
    }
}