package org.bjartek.revealit

import java.io.File


class SlideBuilder(config: Config) {


  def make() = {

    val files: List[List[File]] = buildFileTree()

    val nestedContent: List[List[String]] = files.map(_.map(fetchAndBuildSlide))

    val content = nestedContent.map(x => if (x.size == 1) x.head else "<section>\n" + x.mkString + "</section>\n").mkString

    val template = scala.io.Source.fromFile(config.template).mkString
    template.replace("#name#", config.name)
      .replace("#content#", content)
      .replace("#transition#", config.transition)
  }


  def buildFileTree(): List[List[File]] = {
    val allFiles = config.files.listFiles()
    val presentationFiles = allFiles.filter(file => file.getName.endsWith(".md") || file.getName.endsWith(".html") || file.getName.endsWith(".mermaid"))
    val grouped = presentationFiles.groupBy(file => file.getName.split('.').head)
    val flattend = grouped.map(_._2.toList).toList
    flattend.sortBy(_.head.getName)

  }

  val mermaidBlock: String = """<section class="diagram-slide>
      <span class="diagram-data">
        #content#
      </span>
      <div class="diagram-display"></div>
    </section>"""

  def buildMermaidSlide(s: String): String = {
        mermaidBlock.replace("#content#", s);
  }

  def fetchAndBuildSlide(file: File): String = {
    val text = scala.io.Source.fromFile(file).mkString
    if (file.getName.endsWith(".md")) {
      buildMarkdownSlide(text)
    }else if(file.getName.endsWith(".mermaid")) {
      buildMermaidSlide(text)
    } else {
      buildSlide(text)
    }
  }

  val markdownBlock: String = """<section data-markdown><script type="text/template">
#content#
    </script></section>"""

  def buildMarkdownSlide(content: String) = {
    markdownBlock.replace("#content#", CodeFragmentReplacer(content))
  }


  def buildSlide(content: String) = {
    "\n<section>\n" + CodeFragmentReplacer(content) + "</section>\n"
  }


}

object SlideBuilder {

  def apply(config: Config): String = {
    new SlideBuilder(config).make()
  }
}
