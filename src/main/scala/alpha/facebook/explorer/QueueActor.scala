package alpha.facebook.explorer

import actors.Actor
import model._
import util.matching.Regex
import collection.mutable.Queue

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 13, 2010
 * Time: 9:36:54 PM
 * To change this template use File | Settings | File Templates.
 */

object QueueActor {
  val FacebookIdFormRegex = new Regex("id=(\\d{6,})")
  val FacebookNickNameRegex = new Regex("facebook.com\\/([\\w\\._]+)")

  def isFacebookIdForm(profileUrl: String) = FacebookIdFormRegex.findFirstIn(profileUrl) != None

  def getFacebookId(profileUrl: String) = FacebookIdFormRegex.findFirstMatchIn(profileUrl).get.group(1)

  def getFacebookNickName(profileUrl: String) = FacebookNickNameRegex.findFirstMatchIn(profileUrl).get.group(1)
}

class QueueActor(maxDepth: Int, loadSeenFacebookProfiles: () => List[Profile], saveProfile: Profile => Unit) extends Actor {
  private val seenProfiles = loadSeenFacebookProfiles()
  private var facebookIds = seenProfiles.map(p => (p.facebookId(), p)).toMap
  private var nickNames = seenProfiles.filter(_.nickName() != null).map(p => (p.nickName(), p)).toMap
  private var jobs = Queue[Job]()

  def createJob(p: Profile) = jobs.enqueue(if (p.facebookId() == null) ResolvingNickNameJob(p.nickName()) else MiningJob(p.facebookId()))
  seenProfiles.filter(!_.visited()).foreach(createJob(_))

  def touch(profile: Profile) {
    if (profile.facebookId() != null) {
      if (!facebookIds.contains(profile.facebookId())) {
        facebookIds += (profile.facebookId() -> profile)
        if (profile.depth() <= maxDepth) jobs.enqueue(MiningJob(profile.facebookId()))
        saveProfile(profile)
      }
    } else {
      if (!nickNames.contains(profile.nickName())) {
        nickNames += (profile.nickName() -> profile)
        if (profile.depth() <= maxDepth) jobs.enqueue(ResolvingNickNameJob(profile.nickName()))
        saveProfile(profile)
      }
    }

    println(jobs)
  }

  def touchNickname(nickName: String, displayName: String, depth: Int) {
    val profile = new Profile(null, nickName, displayName, depth)
    touch(profile)
  }

  def touchFacebookId(facebookId: String, displayName: String, depth: Int) {
    val profile = new Profile(facebookId, null, displayName, depth)
    touch(profile)
  }

  def act {
    while (true) {
      receiveWithin(10) {
        case ProfilesResult(facebookId, profiles) => {
          val depth = if (facebookId == null) 0 else facebookIds(facebookId).depth.getValue + 1
          profiles.foreach(p => {
            println(p)
            if (QueueActor.isFacebookIdForm(p._1)) {
              touchFacebookId(QueueActor.getFacebookId(p._1), p._2, depth)
            } else touchNickname(QueueActor.getFacebookNickName(p._1), p._2, depth)
          })

          if (facebookId != null) {
            val profile = facebookIds(facebookId)
            profile.visited := true
            saveProfile(profile)
          }
        }

        case NicknameToFacebookIdResult(nickName, facebookId) => {
          var profile = nickNames(nickName)
          profile.facebookId := facebookId
          touch(profile)
        }

        case ReadySignal() => {
          //          println("catch ReadySignal from " + sender)
          if (jobs.length > 0) {
            //            println("send " + jobs.head)
            sender ! jobs.dequeue()
          } else {
            sender ! NoJob()
          }
        }

        case StopSignal => {
	  println("QueueActor: received StopSignal. Exiting...")
          exit()
        }

        case _ => 
      }
    }
  }
}
