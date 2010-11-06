package alpha.facebook.explorer

import actors.Actor
import model._

class MinerActor(miner: Miner, queueActor: Actor) extends Actor{
    def act {
          miner.login
          while (true) {
            queueActor ! ReadySignal()
            receiveWithin(10) {
              case StopSignal() => {
		println("MinerActor: received StopSignal. Exiting...")
		exit()
	      }
              case MiningJob(facebookId) => {
                val profiles = miner.getFriendProfiles(facebookId)
                sender ! ProfilesResult(facebookId, profiles)
              }
              case ResolvingNickNameJob(nickName) => {
		val facebookId = miner.getFacebookIdByNickName(nickName)
                sender ! NicknameToFacebookIdResult(nickName, facebookId)
              }
              case _ =>
            }
          }
        }
}
