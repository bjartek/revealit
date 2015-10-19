name := "RevealIT"

version := "1.1"

scalaVersion := "2.11.7"

libraryDependencies += "com.github.scopt" %% "scopt" % "3.3.0"

libraryDependencies+= "org.apache.commons" % "commons-lang3" % "3.4"

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.6.5" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers += Resolver.sonatypeRepo("public")

assemblyJarName in assembly := "revealit.jar"
