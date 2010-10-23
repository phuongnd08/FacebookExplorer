package alpha.facebook.explorer

import org.scalatest.matchers.MustMatchers
import org.openqa.selenium.firefox.FirefoxDriver
import org.scalatest.{BeforeAndAfterEach, Spec}
import org.openqa.selenium.{WebDriverBackedSelenium, WebElement, WebDriver, By}

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 8:13:32 PM
 * To change this template use File | Settings | File Templates.
 */

class FirefoxDriverSpec extends Spec with MustMatchers with BeforeAndAfterEach {
  var driver: FirefoxDriver = _

  override def beforeEach {
    driver = new FirefoxDriver
  }

  describe("FirefoxDriver") {
    it("must return correct page title") {
      // Go to the Google Suggest home page
      driver get "http://www.google.com/webhp?complete=1&hl=en"

      println(driver.getTitle)
      // Enter the query string "Cheese"
      val query = driver.findElement(By.name("q"))
      query sendKeys "cheese"
      query.submit
      var wait = 0
      while (driver.getTitle != "cheese - Google Search" && wait <1000){
        Thread.sleep(100)
        wait += 100
      }
      driver.getTitle must be("cheese - Google Search")
      println(driver.getCurrentUrl)
    }
  }

  override def afterEach {
    driver.close
  }
}