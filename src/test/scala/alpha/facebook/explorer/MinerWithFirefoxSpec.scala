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

class MinerWithFirefoxSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var driver: WebDriver = _
  var miner: Miner = _

  override def beforeEach {
    driver = new FirefoxDriver
    miner = new Miner(driver)
  }

  describe("login using firefox") {
    it("should show owner name") {
      miner.login
      driver.findElement(By.className("fbxWelcomeBoxName")).getText must be("Cold Alpha")
    }
  }

  describe("get facebook id from nickName using firefox") {
    it("should report correct facebook id") {
      miner.login
	Thread.sleep(10000)	
      miner.getFacebookIdByNickName("hotanhung") must be("1408525100")
    }
  }

  override def afterEach {
    driver.close
  }
}
