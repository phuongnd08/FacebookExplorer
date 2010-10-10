package alpha.facebook.explorer

import collection.mutable.HashMap
import org.openqa.selenium.htmlunit.HtmlUnitDriver
import org.openqa.selenium.firefox.FirefoxDriver
import ru.circumflex.core._
import org.openqa.selenium.WebDriver

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 4:28:06 PM
 * To change this template use File | Settings | File Templates.
 */

object FacebookExplorer extends HashMap[String, String] {
  import java.util.{ResourceBundle, Locale}
  // The configuration object is initialized by reading `cx.properties`.
  try {
    val bundle = ResourceBundle.getBundle(
      "fe", Locale.getDefault, Thread.currentThread.getContextClassLoader)
    val keys = bundle.getKeys
    while (keys.hasMoreElements) {
      val k = keys.nextElement
      this(k) = bundle.getString(k)
    }
  } catch {
    case _ => FE_LOG.error("Could not read configuration parameters from fe.properties.")
  }

  def getDriver:WebDriver = {
    this("fe.browser") match {
      case "htmlunit" => new HtmlUnitDriver
      case "firefox" => new FirefoxDriver
      case _ => new FirefoxDriver
    }
  }
}
