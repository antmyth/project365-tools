name := "hello"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies += "com.github.melrief" %% "purecsv" % "0.0.6"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"
