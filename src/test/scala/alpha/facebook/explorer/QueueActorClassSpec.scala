package alpha.facebook.explorer

import actors.Actor
import model._
import org.scalatest.matchers.MustMatchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Spec}
import ru.circumflex.orm.DDLUnit

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 8:13:32 PM
 * To change this template use File | Settings | File Templates.
 */

class QueueActorClassSpec extends Spec with MustMatchers with BeforeAndAfterEach with BeforeAndAfterAll {
  var queueActor: QueueActor = _
  var savedProfiles: List[Profile] = _

  override def beforeAll {
    (new DDLUnit(Profile)).create
  }

  override def afterAll {
    (new DDLUnit(Profile)).drop
  }

  override def beforeEach {
    savedProfiles = List[Profile]()
    def saveProfile(profile: Profile) {
      savedProfiles = profile :: savedProfiles
    }
    queueActor = new QueueActor(1, () => List[Profile](), saveProfile)
    queueActor.start
  }

  describe("receive profiles result") {
    it("must create profiles and create new jobs but do not repeat jobs") {
      object actor extends Actor {
        var jobs = List[Job]()

        def act {
          while (true) {
            queueActor ! ReadySignal()
            receiveWithin(10) {
              case StopSignal() => exit()
              case job: Job => {
                if (job != NoJob()) jobs = job :: jobs
              }
              case _ =>
            }
          }
        }
      }


      queueActor ! ProfilesResult(null, List(("http://facebook.com?id=123456", "First User"), ("http://facebook.com/hotanhung", "Hung Reo"), ("http://facebook.com?id=234567", "Second User"), ("http://facebook.com?id=123456", "First User")))
      actor.start
      Thread.sleep(100)
      actor ! StopSignal()

      //must generate proper jobs
      actor.jobs.reverse must be(List(MiningJob("123456"), ResolvingNickNameJob("hotanhung"), MiningJob("234567")))

      //must generate proper profiles
      savedProfiles.reverse must be(List(
        new Profile("123456", null, "First User", 0),
        new Profile(null, "hotanhung", "Hung Reo", 0),
        new Profile("234567", null, "Second User", 0)))
    }
  }

  describe("receive nickname to facebook id signal") {
    it("must queue a new job if facebook id is not seen and create a new profile") {
      queueActor ! ProfilesResult(null, List(("http://facebook.com/abc", "Abc User")))
      Thread.sleep(10)
      queueActor ! ReadySignal()
      queueActor ! NicknameToFacebookIdResult("abc", "1234567")
      object actor extends Actor {
        var jobs = List[Job]()

        def act {
          while (true) {
            queueActor ! ReadySignal()
            receiveWithin(10) {
              case StopSignal() => exit()
              case job: Job => {
                if (job != NoJob()) jobs = job :: jobs
              }
              case _ =>
            }
          }
        }
      }
      actor.start
      Thread.sleep(100)
      actor ! StopSignal()
      actor.jobs must be(List(MiningJob("1234567")))
    }
  }

  describe("act within a maxDepth limit") {
    it("do not queue more job if depth is greater than 1 but still save profiles") {
      queueActor ! ProfilesResult(null, List(("http://facebook.com/?id=100000", "Abc User")))
      object actor extends Actor {
        var miningJobs = List[MiningJob]()

        def act {
          while (true) {
            queueActor ! ReadySignal()
            receiveWithin(10) {
              case StopSignal() => exit()
              case MiningJob(facebookId) => {
                miningJobs = MiningJob(facebookId) :: miningJobs
                var profiles = List[(String, String)]()
                for (i <- 1 to 2) {
                  profiles = ("http://facebook.com?id=" + facebookId + i, facebookId + i) :: profiles
                }
                profiles = ("http://facebook.com/s" + facebookId, "Friend of " + facebookId) :: profiles
                sender ! ProfilesResult(facebookId, profiles.reverse)
              }
              case ResolvingNickNameJob(nickName) => {
                sender ! NicknameToFacebookIdResult(nickName, nickName.drop(1) + "5")
              }
              case _ =>
            }
          }
        }
      }

      actor.start
      Thread.sleep(100)
      actor ! StopSignal()
      actor.miningJobs.map(_.facebookId).reverse must be(
        List("100000", "1000001", "1000002", "1000005"))
      savedProfiles.map(p=>p.facebookId()+"-"+p.nickName()).reverse must be(
        List("100000-null", "1000001-null", "1000002-null", "1000005-s100000",
          "100000-null", "10000011-null", "10000012-null", "null-s1000001",
          "1000001-null", "10000021-null", "10000022-null", "null-s1000002",
          "1000002-null", "1000005-s100000", "10000051-null", "10000052-null",
          "null-s1000005", "1000005-s100000")
        )
    }
  }



  override def afterEach {
    queueActor ! StopSignal()
  }
}
