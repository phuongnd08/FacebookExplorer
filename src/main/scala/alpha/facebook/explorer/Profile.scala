package alpha.facebook.explorer

import ru.circumflex.orm._

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 9, 2010
 * Time: 5:00:24 PM
 * To change this template use File | Settings | File Templates.
 */

class Profile extends Record[Profile]{
  //val id = "id".BIGINT
  val url = "url".VARCHAR(256).NOT_NULL.UNIQUE
  val name = "name".VARCHAR(256).NOT_NULL
  val birthDay = "birth_day".DATE
  //def PRIMARY_KEY = id
  //def relation = Profile
}

object Profile extends Table[Profile]{
  
}