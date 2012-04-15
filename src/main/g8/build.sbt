organization := "$package$"

name := "$name$"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.2"

crossPaths := false

libraryDependencies += "org.scala-sbt" % "command" % "0.12.0-Beta2"

resolvers <+= sbtResolver

initialCommands := """import sbt.complete._
import DefaultParsers._
"""
