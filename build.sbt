name := """hello-slick-specs2"""

organization := "pl.japila"

version := "1.0"

scalaVersion := "2.11.4"

mainClass in Compile := Some("HelloSlick")

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick"       % "3.0.0-M1",
  "org.slf4j"          %  "slf4j-nop"   % "1.7.9",
  "com.h2database"     %  "h2"          % "1.4.184",
  "org.specs2"         %% "specs2-core" % "2.4.15" % "test"
)

scalacOptions += "-deprecation"