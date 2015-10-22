package org.bjartek.revealit

import java.io.File

import org.specs2.mutable._

class MermaidSpec extends Specification {
  "SlideBuilder" should {


    val config = Config(
      files = new File("src/test/resources/files"),
      template = new File("src/test/resources/template.html"),
      resultFile = new File("src/test/resources/result.html"))

    val slideBuilder = new SlideBuilder(config)

    "compile meraid files" in {
      val mermaidFile = new File("src/test/resources/files/3.mermaid")
      val slide = slideBuilder.fetchAndBuildSlide(mermaidFile)

      val expectedSlide =
        """<section class="diagram-slide">
      <span class="diagram-data">
       graph LR
  A --> B
      </span>
      <div class="diagram-display"></div>
    </section>"""

      slide.trim() must beEqualTo(expectedSlide).ignoreSpace

    }

  }
}