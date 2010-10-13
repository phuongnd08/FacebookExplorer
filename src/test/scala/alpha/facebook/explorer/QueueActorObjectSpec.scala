package alpha.facebook.explorer

import org.scalatest.matchers.MustMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 8:13:32 PM
 * To change this template use File | Settings | File Templates.
 */

class QueueActorSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var queueActor: QueueActor = _

  override def beforeEach {
    queueActor = new QueueActor(null, null)
    queueActor.start
  }

  describe("receive found friends") {
    it("must create new tasks") {
      queueActor ! FoundFriendsSignal(List("profile1", "profile2", "profilex"))
      //Thread.sleep(100)
    }
  }

  describe("receive nickname to facebook id signal") {
    it("must process shit accordingly") {
      queueActor ! NicknameToFacebookIdSignal("abc", "123567")
      //Thread.sleep(100)
    }
  }


  override def afterEach {
    queueActor ! StopSignal()
  }
}