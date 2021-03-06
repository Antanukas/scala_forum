import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object ScalaForumBuild extends Build {
  val Organization = "com.home"
  val Name = "Scala Forum"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.3"
  val ScalatraVersion = "2.3.0-SNAPSHOT"

  lazy val project = Project (
    "scala-forum",
    file("."),
    settings = seq(com.typesafe.startscript.StartScriptPlugin.startScriptForClassesSettings: _*) ++ Defaults.defaultSettings ++ ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      //resolvers +=  "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository",
      resolvers += "Sonatype Nexus Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots", //scalatra dev version
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "org.scalatra" %% "scalatra-atmosphere" % ScalatraVersion,
        "org.scalatra" %% "scalatra-json" % ScalatraVersion,
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.14.v20131031" % "compile;container",
        "org.eclipse.jetty" % "jetty-websocket" % "8.1.14.v20131031" % "compile;container",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "compile;container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar")),
        "org.scalatra" %% "scalatra-auth" % "2.2.2",
        "com.github.nscala-time" %% "nscala-time" % "0.8.0",
        "org.json4s"   %% "json4s-jackson" % "3.2.6"

  ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}
