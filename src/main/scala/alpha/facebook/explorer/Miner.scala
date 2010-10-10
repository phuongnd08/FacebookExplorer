package alpha.facebook.explorer

import org.openqa.selenium.{By, WebDriver}

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 10:18:14 PM
 * To change this template use File | Settings | File Templates.
 */

class Miner(val driver:WebDriver){
  def login {
    driver.get("http://facebook.com")
    driver.findElement(By.id("email")).sendKeys(fe("fe.email"))
    driver.findElement(By.id("pass")).sendKeys(fe("fe.password"))
    driver.findElement(By.id("pass")).submit
  }
}