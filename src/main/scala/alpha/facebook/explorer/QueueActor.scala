package alpha.facebook.explorer

import actors.Actor
import model._
import util.matching.Regex
import org.mortbay.thread.Timeout.Task
import collection.mutable.{Queue, HashSet}

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
  private var seenProfilesMap = seenProfiles.map(p => (p.facebookId(), p)).toMap
  private var seenNicknames = Map[String /*nickName*/ , (String /*displayName*/ , Int /*depth*/ )]()
  private var jobs = Queue[Job]()
  seenProfiles.filter(!_.visited()).foreach(p => jobs.enqueue(MiningJob(p.facebookId())))

  def touchFacebookId(facebookId: String, displayName: String, depth: Int) {
    println("touch: " + facebookId)
    if (!seenProfilesMap.contains(facebookId)) {
      var profile = new Profile(facebookId, displayName, depth)
      saveProfile(profile)
      seenProfilesMap = seenProfilesMap + (facebookId -> profile)
      if (!(depth > maxDepth))
        jobs.enqueue(new MiningJob(facebookId))
    }
  }

  def touchNickname(nickName: String, displayName: String, depth: Int) {
    println("touch: " + nickName)
    if (!seenNicknames.contains(nickName)) {
      seenNicknames = seenNicknames + (nickName -> (displayName, depth))
      jobs.enqueue(new ResolvingNickNameJob(nickName))
    }
  }

  def act {
    while (true) {
      receive {
        case ProfilesResult(facebookId, profiles) => {
          val depth = if (facebookId == null) 0 else seenProfilesMap(facebookId).depth.getValue + 1
          profiles.foreach(p => {
            println(p)
            if (QueueActor.isFacebookIdForm(p._1)) {
              touchFacebookId(QueueActor.getFacebookId(p._1), p._2, depth)
            } else touchNickname(QueueActor.getFacebookNickName(p._1), p._2, depth)
          })
        }

        case NicknameToFacebookIdResult(nickName, facebookId) => {
          touchFacebookId(facebookId, seenNicknames(nickName)._1, seenNicknames(nickName)._2)
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
          exit()
        }

        case _ =>
      }
    }
  }
}