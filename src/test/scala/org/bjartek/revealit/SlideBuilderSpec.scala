package org.bjartek.revealit

import org.specs2.mutable._
import java.io.File
import collection.immutable.List

class SlideBuilderSpec extends Specification {

  "SlideBuilder" should {

    val config = Config(
      files = new File("src/test/resources/files"),
      template = new File("src/test/resources/template.html"),
      resultFile = new File("src/test/resources/result.html"))

    val slideBuilder = new SlideBuilder(config)

    "build file tree" in {
      val tree = slideBuilder.buildFileTree()

      tree must equalTo(List(
        List(new File("src/test/resources/files/0.1.html"), new File("src/test/resources/files/0.2.html"), new File("src/test/resources/files/0.3.html")),
        List(new File("src/test/resources/files/1.md")),
        List(new File("src/test/resources/files/2.html"))))
    }

  }
}
