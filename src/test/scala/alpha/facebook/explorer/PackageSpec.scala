package alpha.facebook.explorer

import org.scalatest.matchers.MustMatchers
import ru.circumflex.orm._

import java.util.Date
import org.scalatest.{BeforeAndAfterAll, Spec}
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.htmlunit.HtmlUnitDriver

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 9, 2010
 * Time: 6:15:13 PM
 * To change this template use File | Settings | File Templates.
 */

class PackageSpec extends Spec with MustMatchers {
  describe("fe") {
    it("must is an alias of FacebookExplorer") {
      fe must be(FacebookExplorer)
    }

    it("return the value of desired browser") {
      fe("fe.browser") must be("Firefox")
    }

    it("return firefox driver according to fe.browser value") {
      fe("fe.browser") = "firefox"
      val ffDriver = fe.getDriver
      ffDriver.isInstanceOf[FirefoxDriver] must be(true)
      ffDriver.close
    }

    it("return htmlunit driver according to fe.browser value") {
      fe("fe.browser") = "htmlunit"
      val huDriver = fe.getDriver
      huDriver.isInstanceOf[HtmlUnitDriver] must be(true)
      huDriver.close
    }
  }
}