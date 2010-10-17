package alpha.facebook.explorer

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 16, 2010
 * Time: 4:55:43 PM
 * To change this template use File | Settings | File Templates.
 */

abstract class Result
case class ProfilesResult(facebookId:String, profiles:List[(String/*url*/, String/*display name*/)]) extends Result
case class NicknameToFacebookIdResult(nickName:String, facebookId:String) extends Result