import AssemblyKeys._ // put this at the top of the file

name := "RevealIT"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.0-RC2"

libraryDependencies += "com.github.scopt" % "scopt_2.10.0-RC2" % "2.1.0"

libraryDependencies += "org.specs2" %% "specs2" % "1.12.3" % "test"

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                    "releases"  at "http://oss.sonatype.org/content/repositories/releases")

assemblySettings

jarName in assembly := "revealit.jar"

target in assembly := new File("revealit")

//comment in this to exclude scala from jar
//assembleArtifact in packageScala := false

