package alpha.facebook.explorer.model

import ru.circumflex.orm.{Table, Record}

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 11, 2010
 * Time: 9:13:56 PM
 * To change this template use File | Settings | File Templates.
 */

abstract class Job
case class NoJob extends Job
case class ResolvingNickNameJob(nickName:String) extends Job
case class MiningJob(facebookId:String) extends Job
