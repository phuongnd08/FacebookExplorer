package alpha.facebook.explorer.model

import ru.circumflex.orm._

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 9, 2010
 * Time: 5:00:24 PM
 * To change this template use File | Settings | File Templates.
 */

class Profile extends Record[Profile] {
  //val id = "id".BIGINT
  val nickName = "nick_name".VARCHAR(100).NULLABLE
  val facebookId = "facebook_id".VARCHAR(20).NULLABLE
  val displayName = "display_name".VARCHAR(256).NOT_NULL
  val birthDay = "birth_day".DATE.NULLABLE
  val depth = "depth".INTEGER.DEFAULT("0").NOT_NULL
  val visited = "visited".BOOLEAN.DEFAULT("false").NOT_NULL
  //def PRIMARY_KEY = id
  //def relation = Profile
  def this(facebookId: String) = {
    this ()
    this.facebookId := facebookId
  }

  def this(facebookId: String, nickName:String, displayName: String, depth: Int) = {
    this (facebookId)
    this.nickName := nickName
    this.displayName := displayName
    this.depth := depth
  }

  override def toString = {
    displayName() + "(id=" + facebookId() + ", nickName=" + nickName() + ", depth=" + depth() + ")"
  }
}

object Profile extends Table[Profile] {
}
