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

class ProfileSpec extends Spec with MustMatchers with BeforeAndAfterAll {
  override def beforeAll {
    val ddl = new DDLUnit(Profile)
    ddl.create
    println(ddl.messages(0))
  }

  override def afterAll {
    val ddl = new DDLUnit(Profile)
    ddl.drop
  }
  describe("create and list") {
    it("must list all created profiles") {
      val p1 = new Profile
      p1.facebookId := "1"
      p1.displayName := "First User"
      p1.birthDay := new Date("2010/10/9")

      val p2 = new Profile
      p2.displayName := "Second User"
      p2.facebookId := "2"
      p2.nickName := "second_user"
      p2.birthDay := new Date("2010/10/9")

      p1.save()
      p2.save()

      val pr = Profile as "pr"
      val list = (SELECT(pr.*) FROM pr).list()
      list must have length (2)

      println(list)
    }
  }

  describe("toString") {
    it("must produce helpful information") {
      val p2 = new Profile
      p2.displayName := "Second User"
      p2.facebookId := "2"
      p2.nickName := "second_user"
      p2.birthDay := new Date("2010/10/9")
      p2.depth := 3

      p2.toString must be("Second User(id=2, nickName=second_user, depth=3)")
    }
  }
}