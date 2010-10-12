package alpha.facebook.explorer.model

import org.scalatest.matchers.MustMatchers
import ru.circumflex.orm._

import java.util.Date
import org.scalatest.{BeforeAndAfterAll, Spec}

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 9, 2010
 * Time: 6:15:13 PM
 * To change this template use File | Settings | File Templates.
 */

class TaskSpec extends Spec with MustMatchers with BeforeAndAfterAll{

  override def beforeAll{
    val ddl = new DDLUnit(Task)
    ddl.create
    println(ddl.messages(0))
  }

  override def afterAll{
    val ddl = new DDLUnit(Task)
    ddl.drop
  }
  describe("create and list"){
    it("must list all created undone task"){
      val t1 = new Task
      t1.facebookId := "123"
      t1.depth := 1
      t1.done := false
      val t2 = new Task
      t2.facebookId := "234"
      t2.depth := 2
      t2.done := true

      t1.save()
      t2.save()

      val tk = Task as "tk"
      val list = (SELECT (tk.*) FROM tk WHERE (tk.done EQ false)).list()
      list must have length (1)
      println(list)
    }
  }
}