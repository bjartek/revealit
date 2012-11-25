package org.bjartek.revealit

import java.io.{FileNotFoundException, FileWriter, File}


case class Config(
                   resultFile: File = new File("index.html"),
                   files: File = new File("slides"),
                   name: String = "Reveal.js Presentation",
                   transition: String = "default",
                   template: File = new File("template.html")
                   ) {


  def parseTransition(t: String) = {
    if (SlideRunner.transitions.contains(t)) {
      this.copy(transition = t)
    } else {
      throw new IllegalArgumentException("Transition [" + t + "] not one of " + SlideRunner.transitions.mkString(","))
    }
  }

  def parseFiles(f: String) = {
    val file = new File(f)
    if (file.isDirectory && file.canRead) {
      this.copy(files = file)
    } else {
      throw  filesError(file)
    }
  }

  def filesError(file:File) =   new FileNotFoundException("Files [" + file.getName + "] is not a readable directory")


  def validateConfig(): Either[Throwable, Boolean] = {

    if (!files.isDirectory || !files.canRead) {
      Left(filesError(files))
    } else if (!template.isFile || !template.canRead) {
      Left(new FileNotFoundException("Cannot read template file [" + template.getName + "]"))
    } else {
      Right(true)
    }
  }

}

object SlideRunner extends App {

  import scopt.immutable.OptionParser
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
    def options = Seq(
      opt("s", "slides", "Where to find the files with slides default: " + defaultConfig.files) {
        (v: String, c: Config) => c.parseFiles(v)
      },
      opt("rf", "resultFile", "Name of result file default: " + defaultConfig.resultFile) {
        (v: String, c: Config) => c.copy(resultFile = new File(v))
      },
      opt("n", "name", "Title of HTML page default: " + defaultConfig.name) {
        (v: String, c: Config) => c.copy(name = v)
      },
      opt("t", "transition", "Transition. One of " + transitions.mkString("|") + " default: " + defaultConfig.transition) {
        (v: String, c: Config) => c.parseTransition(v)
      }
      ,
      opt("tpl", "template", "Template to use  default: " + defaultConfig.template) {
        (v: String, c: Config) => c.copy(template = new File(v))
      }
    )
  }

  parser.parse(args, Config()) map {
    config => {
      config.validateConfig() match {
        case Left(error) => println(error.getMessage); parser.showUsage
        case Right(_) => {
          using(new FileWriter(config.resultFile)) {
            _.write(new SlideBuilder(config).make())
          }

        }
      }
    }
  }
}


