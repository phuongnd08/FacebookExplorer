package alpha.facebook.explorer

import org.scalatest.matchers.MustMatchers
import org.scalatest.Spec
import org.openqa.selenium.By
import org.openqa.selenium.htmlunit.HtmlUnitDriver

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 8:13:32 PM
 * To change this template use File | Settings | File Templates.
 */

class HtmlUnitDriverSpec extends Spec with MustMatchers {
  describe("HtmlUnitDriver") {
    it("must return correct page title") {
      val driver = new HtmlUnitDriver
      driver.setJavascriptEnabled(true)

      // Go to the Google Suggest home page
      driver get "http://www.google.com/webhp?complete=1&hl=en"

      // Enter the query string "Cheese"
      val query = driver.findElement(By.name("q"))
      query sendKeys "cheese"
      query.submit
      driver.getTitle must be("cheese - Google Search")
      println(driver.getCurrentUrl)
      driver.close
    }
  }
}