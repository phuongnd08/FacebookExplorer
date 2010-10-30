package alpha.facebook.explorer

import org.scalatest.matchers.MustMatchers
import org.scalatest.Spec
/**
 * Created by IntelliJ IDEA.
 * User: phuongnd08
 * Date: Oct 10, 2010
 * Time: 8:13:32 PM
 * To change this template use File | Settings | File Templates.
 */

class EnvironmentSpec extends Spec with MustMatchers {
  describe("getJarPath") {
    it("must return current execution path") {
      Environment.getJarPath must be === "/home/phuongnd08/code/facebook-explorer/target/scala_2.8.0/classes/"
    }
  }
}