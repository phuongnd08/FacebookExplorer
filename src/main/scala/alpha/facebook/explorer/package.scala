package alpha.facebook

import explorer.FacebookExplorer
import org.apache.log4j.Logger

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 5:25:45 PM
 * To change this template use File | Settings | File Templates.
 */

package object explorer{
  val fe = FacebookExplorer
  val FE_LOG = Logger.getLogger("alpha.facebook.explorer")
}