package alpha.facebook.explorer.model

import ru.circumflex.orm.{Table, Record}

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 11, 2010
 * Time: 9:13:56 PM
 * To change this template use File | Settings | File Templates.
 */

class Task extends Record[Task]{
  //val id = "id".BIGINT
  val facebookId = "facebook_id".VARCHAR(20)
  val depth = "depth".INTEGER
  val done = "done".BOOLEAN
  //def PRIMARY_KEY = id
  //def relation = Profile
}

object Task extends Table[Task]{

}