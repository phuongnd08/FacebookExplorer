package alpha.facebook.explorer

import actors.Actor
import model.Task
import util.matching.Regex

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
class QueueActor(loadNextTasks: Int => List[Task], storeTask: Task => Unit) extends Actor {
  def act {
    loop {
      react {
        case FoundFriendsSignal(profiles) => {
          println("Found: " + profiles)
        }

        case NicknameToFacebookIdSignal(nickName, facebookId) => {
          println("Converted: " + nickName + "=>" + facebookId)
        }

        case StopSignal => {
          exit()
        }
      }
    }
  }
}