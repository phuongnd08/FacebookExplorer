import sbt._

class AutoPostProject(info: ProjectInfo) extends ProguardProject(info) with IdeaProject
{
  val mavenLocal = "Local Maven Repository" at "file://" + Path.userHome + "/.m2/repository"
  var biblioMirror = "Biblio Mirror" at "http://mirrors.ibiblio.org/pub/mirrors/maven2/"
  val mavenRepo = "Maven Repo" at "http://repo2.maven.org/maven2"
  val azeckoskiRepo = "Azeckoski Repo" at "https://source.sakaiproject.org/maven2"

  lazy val copyTools = task {
    def copySelenium(target: Path) = FileUtilities.copyFile(info.projectPath / "tools" / "selenium-server-standalone-2.0a5.jar", target / "selenium-server.jar", log)
    copySelenium(info.projectPath / "target" / "scala_2.8.0" / "classes" / "tools")
    copySelenium(info.projectPath / "target" / "scala_2.8.0" / "tools")
  }
  lazy val copyTestSample = task {
    def copyTestSections(target: Path) = FileUtilities.sync(info.projectPath / "test-sections", target, log)
    copyTestSections(info.projectPath / "target" / "scala_2.8.0" / "classes" / "sections")
    copyTestSections(info.projectPath / "target" / "scala_2.8.0" / "sections")
  }

  lazy val copyRealSample = task {
    def copyRealSections(target: Path) = FileUtilities.sync(info.projectPath / "real-sections", target, log)
    copyRealSections(info.projectPath / "target" / "scala_2.8.0" / "classes" / "sections")
    copyRealSections(info.projectPath / "target" / "scala_2.8.0" / "sections")
  }

  override def testAction = super.testAction dependsOn (copyTools, copyTestSample)

  val specs = "org.scalatest" % "scalatest" % "1.2" % "test"
  var snakeYaml = "org.yaml" % "snakeyaml" % "1.7"
  var jodaTime = "joda-time" % "joda-time" % "1.6.1"
  var reflectionUtils = "org.azeckoski" % "reflectutils" % "0.9.14"
  var mockito = "org.mockito" % "mockito-core" % "1.8.1" % "test"
  var circumflexOrm = "ru.circumflex" % "circumflex-orm" % "1.2"
  var mysqlConnector = "mysql" % "mysql-connector-java" % "5.1.13"


  override def mainClass = Some("alpha.autoPost.Main")

  def keepMain(className: String) =
    """-keep public class %s {
    | public static void main(java.lang.String[]);
    |}""".stripMargin format className

  override def allDependencyJars = (super.allDependencyJars +++
          Path.fromFile(buildScalaInstance.compilerJar) +++
          Path.fromFile(buildScalaInstance.libraryJar)
          )

  override def proguardOptions = List(
    "-keep class alpha.** { *; }",
    "-keep class org.azeckoski.reflectutils.** { *; }",
    keepMain("alpha.autoPost.Main"),
    "-dontoptimize",
    "-dontobfuscate",
    proguardKeepLimitedSerializability,
    proguardKeepAllScala,
    "-keep interface scala.ScalaObject"
    )
}
