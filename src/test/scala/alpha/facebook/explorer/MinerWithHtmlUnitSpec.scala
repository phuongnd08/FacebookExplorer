package alpha.facebook.explorer

import org.scalatest.matchers.MustMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.{By, WebDriver}
import org.openqa.selenium.htmlunit.HtmlUnitDriver

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 10:29:46 PM
 * To change this template use File | Settings | File Templates.
 */

class MinerWithHtmlUnitSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var driver: WebDriver = _
  var miner: Miner = _

  override def beforeEach{
    var hud = new HtmlUnitDriver
      hud.setJavascriptEnabled(true)
      driver = hud
      miner = new Miner(driver)    
  }
  describe("login using htmlunit") {
    it("should show owner name") {
      miner.login
      driver.findElement(By.id("profile_name")).getText must be("Cold Alpha")
    }
  }

  override def afterEach {
    driver.close
  }
}