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

class MinerSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var driver: WebDriver = _
  var miner: Miner = _

  describe("login using firefox") {
    it("should show owner name") {
      driver = new FirefoxDriver
      miner = new Miner(driver)
      miner.login
      driver.findElement(By.className("fbxWelcomeBoxName")).getText must be("Cold Alpha")
    }
  }

  describe("login using htmlunit") {
    it("should show owner name") {
      var hud = new HtmlUnitDriver
      hud.setJavascriptEnabled(true)
      driver = hud

      miner = new Miner(driver)
      miner.login
      driver.findElement(By.className("fbxWelcomeBoxName")).getText must be("Cold Alpha")
    }
  }

  override def afterEach {
    driver.close
  }
}
