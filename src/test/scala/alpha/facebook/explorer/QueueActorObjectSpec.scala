package alpha.facebook.explorer

import org.scalatest.matchers.MustMatchers
import org.openqa.selenium.firefox.FirefoxDriver
import org.scalatest.{BeforeAndAfterEach, Spec}
import org.openqa.selenium.By

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 8:13:32 PM
 * To change this template use File | Settings | File Templates.
 */

class QueueActorObjectSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  describe("isFacebookIdForm") {
    it("must return true if url is in facebookId form") {
      QueueActor.isFacebookIdForm("http://www.facebook.com/profile.php?id=123456") must be(true)
    }
    it("must return false if url is not in facebookId form") {
      QueueActor.isFacebookIdForm("http://www.facebook.com/cold.alpha") must be(false)
    }
  }
  describe("getFacebookId") {
    it("must return the correct facebook Id") {
      QueueActor.getFacebookId("http://www.facebook.com/profile.php?id=123456") must be("123456")
    }
  }

  describe("getFacebookNickName") {
    it("must return the correct facebook nick name if no parameter is added") {
      QueueActor.getFacebookNickName("http://www.facebook.com/cold.alpha") must be("cold.alpha")
    }

     it("must return the correct facebook nick name if some parameters is added") {
      QueueActor.getFacebookNickName("http://www.facebook.com/cold.alpha?ref=wow") must be("cold.alpha")
    }
  }

}