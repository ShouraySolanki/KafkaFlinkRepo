name := "Flink-Sample"

version := "0.1"

scalaVersion := "2.11.9"
val flinkVersion = "1.14.0"

// https://mvnrepository.com/artifact/org.apache.flink/flink-streaming-java
libraryDependencies += "org.apache.flink" %% "flink-streaming-scala" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-connector-kafka" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-scala" % flinkVersion
libraryDependencies += "org.apache.flink" %% "flink-clients" % flinkVersion
libraryDependencies += "org.apache.flink" % "flink-core" % flinkVersion
libraryDependencies += "org.apache.flink" % "flink-json" % flinkVersion
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.4"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.4"
libraryDependencies += "org.json4s" %% "json4s-native" % "4.0.2"



