package alpha.facebook.explorer

import org.openqa.selenium.firefox.FirefoxDriver
import org.scalatest.matchers.MustMatchers
import org.openqa.selenium.WebDriver
import org.scalatest.{Spec, BeforeAndAfterEach}

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 12, 2010
 * Time: 11:13:15 PM
 * To change this template use File | Settings | File Templates.
 */

class MinerGetFriendProfilesWithFirefoxSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var driver: WebDriver = _
  var miner: Miner = _

  override def beforeEach {
    driver = new FirefoxDriver
    miner = new Miner(driver)
  }

  describe("get friend profiles using firefox") {
    it("should report all friends profiles") {
      miner.login
      def shorten(url: String) = if (QueueActor.isFacebookIdForm(url)) QueueActor.getFacebookId(url) else QueueActor.getFacebookNickName(url)
      val list = List("phuongaxl", "piper.tram", "quyenbc.fab", "quynhlai", "seal91", "thaivancuong1987", "thanh.viet", "thucanhstrawberry",
        "thy.nguyen2", "tomliz", "tqnam", "tranminhhuy19121987", "vamp.tran")

      val profiles = miner.getFriendProfiles("581175558").map(shorten).toList
      list.foreach(name => profiles must contain(name))
    }
  }

  override def afterEach {
    driver.close
  }
}
