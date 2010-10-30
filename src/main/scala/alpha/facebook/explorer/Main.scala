package alpha.facebook.explorer

import model.Profile
import ru.circumflex.orm.DDLUnit

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 30, 2010
 * Time: 4:54:52 PM
 * To change this template use File | Settings | File Templates.
 */

object Main {
  def printHelp {
    println("Facebook Explorer Application")
    println("Written by phuongnd08")
    println("Command available")
    println("  help: Display this help")
    println("  migrate: Create all necessary tables")
    println("  reset migration: Destroy all tables")
    println("  mine {level=1,2,3...}: Mine upto a specific level")
    println("  register gmail {number_of_account}: register gmail account")
    println("  register facebook: register google account")
    println("  list mining: list collected account")
    println("  list gmail: list registered gmail account")
    println("  list facebook: list registered facebook account")
    println("  exit : quit the program")
  }

  def printHint {
    println("Type help to know how to use")
  }

  val DumpFile = "queue-collector.log"

  def dump(string: String) {
    val fw = new java.io.FileWriter(Environment.getJarPath + "/" + DumpFile, true)
    fw.write(string + "\n")
    fw.flush
    fw.close
  }

  def migrate {
    val ddl = new DDLUnit(Profile)
    ddl.create
    println(ddl.messages(0))
  }

  def reset(params: Array[String]) {
    if (params.length == 1) {
      params(0) match {
        case "migration" => {
          val ddl = new DDLUnit(Profile)
          ddl.drop
          println(ddl.messages(0))
        }
        case _ => {
          println("Cannot reset for " + params(0))
        }
      }
    } else {
      println("Wrong number of arguments")
    }
  }

  def mine(params: Array[String]) {
    if (params.length == 1) {
      try {
        val level = params(0).toInt
        println("Mining at level {" + level + "}")
      }
      catch {
        case e: Exception => {
          println(e)
        }
      }
    } else {
      println("Wrong number of arguments")
    }
  }

  def main(args: Array[String]) {
    printHint
    println("Loading resources from [" + Environment.getJarPath + "]")

    while (true) {
      print("> ")
      val line = readLine
      val command = line.split(" ").head
      val args = line.split(" ").tail
      command match {
        case "help" => printHelp
        case "migrate" => migrate
        case "reset" => reset(args)
        case "mine" => mine(args)
        case "exit" => {
          return
        }
        case _ => println("What the hell are you trying to do?")
      }
    }
  }
}