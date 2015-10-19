package org.bjartek.revealit

import java.io.{File, FileWriter}

import scopt.OptionParser


case class Config(
                   resultFile: File = new File("index.html"),
                   files: File = new File("slides"),
                   name: String = "Reveal.js Presentation",
                   transition: String = "default",
                   template: File = new File("template.html")
                   )

object SlideRunner extends App {

  import scala.language.reflectiveCalls

  val defaultConfig = Config()

  val transitions = List("default", "cube", "concave", "page", "linear", "zoom", "none")


  def using[A <: {def close() : Unit}, B](param: A)(f: A => B): B =
    try {
      f(param)
    } finally {
      param.close()
    }


  val parser = new OptionParser[Config]("java -jar revealit.jar ") {

    opt[File]('s', "slides") validate {
      f: File => if (f.isDirectory && f.canRead) success else failure("Slides directory must be readable")
    } action {
      (x,c) => c.copy(files = x)
    } valueName("<file>") text("Where to find the files with slides default: " + defaultConfig.files)
    opt[File]('r', "resultfile") action {
      (x,c) => c.copy(resultFile = x)
    } valueName("<file>") text("Name of result file default: " + defaultConfig.resultFile)
    opt[String]('n', "name") text("Title of HTML page default: " + defaultConfig.name) action{
      (x,c) => c.copy(name = x)
    }
    opt[String]('t', "transition") text("Transition. One of " + transitions.mkString("|") + " default: " + defaultConfig.transition) validate {
      v: String => if (SlideRunner.transitions.contains(v)) success else failure("Illegal value")
    } action{
      (x,c) => c.copy(transition = x)
    }
    opt[File]("template") abbr("tpl") validate {
      f: File => if (f.canRead) success else failure("Template must be readable file")
    }action {
      (x,c) => c.copy(template = x)
    } valueName("<file>") text("Template to use  default: " + defaultConfig.template)

  }

  parser.parse(args, Config()) match {
    case Some(config) =>  {
      using(new FileWriter(config.resultFile)) {
        _.write(new SlideBuilder(config).make())
      }
    }
    case None =>
  }

}


