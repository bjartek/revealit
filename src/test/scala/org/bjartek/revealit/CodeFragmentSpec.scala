package org.bjartek.revealit
import org.specs2.mutable._

class CodeFragmentSpec extends Specification {

  "CodeFragments" should {

    "insert entire scala file" in {
      val code =
        """
          <code class="scala">src/test/resources/Foo.scala</code>
        """

      val inserted = CodeFragmentReplacer(code)
      inserted must contain("<code class=\"scala\"")
      inserted must contain("class ScalaFoo {\n  println(\"test\")\n}")

    }
    "insert partial java file" in {
      val code = """slide
                     <code class="java">src/test/resources/Foo.java:4-6</code>"""
      val inserted = CodeFragmentReplacer(code)
      inserted must contain("<code class=\"java\">")
      inserted must contain("public static void hello() {\n        System.out.println(\"hello\");\n    }")
    }

    "insert error message on wrong partial spesification" in {
      val code = """slide
                     <code class="java">src/test/resources/Foo.java:6-4</code>"""
      val inserted = CodeFragmentReplacer(code)
      inserted must contain("stop is smaller then start")
    }

    "find all code placeholders " in {

      val content =
        """
            <<code>foo.java</code>

            <code class="java">asd.java</code>

            <code>foo.scala:123-156</code>

            <code class="scala">foo.scala:123-156</code>
        """

      val res = CodeFragmentReplacer.findCodePlaceholders(content)

      res must contain(PartialCodeFragment(">foo.scala:123-156</code>", "foo.scala", 123, 156))
      res must contain(CodeFragmentFile(">foo.java</code>", "foo.java"))
      res must contain(CodeFragmentFile(">asd.java</code>", "asd.java"))
      res must contain(PartialCodeFragment(">foo.scala:123-156</code>", "foo.scala", 123, 156))


    }

    "leave normal code blocks in place " in {

      val content =
        """
            <<code class="scala">println("foo")</code>

        """

      val res = CodeFragmentReplacer(content)

      res must contain("""<code class="scala">println("foo")</code>""")



    }

    "escape special chars in code" in {
        val code =
          """
          <code class="scala">src/test/resources/Escape.java</code>
          """

        val inserted = CodeFragmentReplacer(code)
        inserted must contain("List&lt;String&gt;")



    }

  }

}
