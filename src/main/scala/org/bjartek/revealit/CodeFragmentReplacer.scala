package org.bjartek.revealit

import io.BufferedSource

object CodeFragmentReplacer {

  def apply(content: String) = {
    val codes = findCodePlaceholders(content)
    val replacedContent = codes.foldLeft(content)((c, code) => code.replace(c))
    replacedContent.mkString
  }


  def findCodePlaceholders(content: String) = {
    val re = """>([\w/\\]+)\.(\w+)(:(\d+)-(\d+))?</code>""".r
    val matches = re.findAllMatchIn(content).toList

    matches.map {
      m =>
        if (m.subgroups(2) == null) {
          CodeFragmentFile(m.matched, m.subgroups(0) + "." + m.subgroups(1))
        } else {
          PartialCodeFragment(m.matched, m.subgroups(0) + "." + m.subgroups(1), m.subgroups(3).toInt, m.subgroups(4).toInt)
        }
    }
  }
}
sealed trait CodeFragment {
  def handleCodeFile(content: BufferedSource): String

  def filename: String

  def placeholder: String

  def replace(content: String): String = {
    val code = readFile(filename) match {
      case Right(c) => ">" + handleCodeFile(c) + "</code>"
      case Left(error) => ">" + error.getMessage + "</code>"
    }
    content.replace(placeholder, code)
  }


  def readFile(fileName: String): Either[Throwable, BufferedSource] = {
    try {
      Right(io.Source.fromFile(fileName))
    } catch {
      case e: Exception => Left(e)
    }
  }

}

case class CodeFragmentFile(placeholder:String, filename: String) extends CodeFragment {
  override def handleCodeFile(content: BufferedSource): String =  content.mkString
}

case class PartialCodeFragment(placeholder: String, filename: String, start: Int, stop: Int) extends CodeFragment {
  override def handleCodeFile(content: BufferedSource): String = {
    if (stop < start) {
       placeholder + " error: invalid range. stop is smaller then start"
    } else {
      content.getLines().drop(start.toInt - 1).take(stop.toInt - start.toInt + 1).mkString("\n")
    }
  }
}
