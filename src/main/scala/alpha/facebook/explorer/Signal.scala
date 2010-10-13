package alpha.facebook.explorer

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 13, 2010
 * Time: 9:56:20 PM
 * To change this template use File | Settings | File Templates.
 */

abstract class Signal
case class StopSignal extends Signal
case class FoundFriendsSignal(profiles:List[String]) extends Signal
case class ProfileSignal(facebookId:String) extends Signal
case class NicknameSignal(nickName:String) extends Signal
case class NicknameToFacebookIdSignal(nickName:String, facebookId:String) extends Signal