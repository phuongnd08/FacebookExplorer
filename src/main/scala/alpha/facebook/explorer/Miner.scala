package alpha.facebook.explorer

import model.Profile
import org.openqa.selenium.{By, WebDriver, Keys}
import collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 10:18:14 PM
 * To change this template use File | Settings | File Templates.
 */

object Miner {
  val IdMatcher = new util.matching.Regex("id=(\\d{6,})")
}

class Miner(val driver: WebDriver) {
  def login {
    driver.get("http://facebook.com")
    driver.findElement(By.id("email")).sendKeys(fe("fe.email"))
    driver.findElement(By.id("pass")).sendKeys(fe("fe.password"))
Thread.sleep(1000)
    driver.findElement(By.id("email")).submit
  }

  def getFacebookIdByNickName(nickName: String): String = {
    driver.get("http://facebook.com/" + nickName)
    val href = driver.findElement(By.xpath("//div[h5='Friends']")).findElement(By.linkText("See All")).getAttribute("href")
    Miner.IdMatcher.findFirstMatchIn(href).get.group(1)
  }

  def getFriendProfiles(facebookId: String): List[String] = {
    var result = List[String]()
    driver.get("http://www.facebook.com/profile.php?id=" + facebookId)
    //show all friends
    driver.findElement(By.xpath("//div[h5='Friends']")).findElement(By.linkText("See All")).click
    def getFriendsDialog = {
      val nodes = driver.findElements(By.id("pb_" + facebookId + "_object_browser_content_area"))
      println("getFriendsDialog => "+nodes)
      if (nodes.length > 0) nodes(0)
      else null
    }
    //wait for dialog
    while (getFriendsDialog == null) {Thread.sleep(100)}
    val friendsDialog = getFriendsDialog
    def waitForNextPage {
      var orgContent = friendsDialog.getText
      do {
        Thread.sleep(100)
      } while (orgContent != friendsDialog.getText)
    }
    def getNextButton = driver.findElement(By.id("pb_"+facebookId+"_object_browser_pager_more"))
    def canGoNext = getNextButton != null
    def goNext = getNextButton.click
    var continue = true
    while (continue) {
	var links = friendsDialog.findElements(By.xpath(".//a"))
	while (links.length == 0) {
		println("getLinks => "+links)
		Thread.sleep(100)
		links = friendsDialog.findElements(By.xpath(".//a"))
	}
	for (time <- 0 until 20){
	links(0).sendKeys(Keys.PAGE_DOWN)
        Thread.sleep(100)
}
      friendsDialog.findElements(By.className("UIObjectListing_Title")).foreach(e => {result = e.getAttribute("href") :: result; println(e.getAttribute("href"))})
	println("Found "+result.length+" friends")
      if (canGoNext) {
        println(getNextButton)
        goNext
        waitForNextPage
      }
      else continue = false
    }
    result.sorted
  }
}
