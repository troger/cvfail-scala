package controllers

import models.Fail
import play.data.validation.{Required, Validation}
import play.mvc.{ScalaController, Controller}
import play.cache.Cache
import play.libs.{Codec, Images}

/**
 * @author <a href="mailto:troger@nuxeo.com">Thomas Roger</a>
 */

object FailController extends ScalaController {

  val FailsPerPage = 10

  def index(offset: Int = 0) = {
    val fails = Fail.find("order by postedAt desc").from(offset).fetch(FailsPerPage)
    val failsCount = Fail.count
    val oldest = failsCount - FailsPerPage
    val older: Any = offset + FailsPerPage
    val newer: Any = if (FailsPerPage > offset) 0 else offset - FailsPerPage
    val showOlderLink = offset < failsCount - FailsPerPage
    val showOldestLink = showOlderLink
    val userId = session.get("user")
    val authenticated = session.contains("user")
    render(fails, offset.asInstanceOf[Object],
          oldest.asInstanceOf[Object], older.asInstanceOf[Object],
          newer.asInstanceOf[Object], showOlderLink.asInstanceOf[Object],
          showOldestLink.asInstanceOf[Object], authenticated.asInstanceOf[Object],
          userId)
  }

  def create = {
    if (!session.contains("user")) {
      Application.login
    }
    val randomId = Codec.UUID
    render(randomId)
  }

  def postFail(@Required(message = "A message is required") message: String,
               @Required(message = "Please type the code") code: String,
               sarcasm: String, randomId: String) = {
    validation.equals(code, Cache.get(randomId)).message("Invalid code. Please type it again")

    if (Validation.hasErrors()) {
      render("FailController/create.html")
    }

    val newFail: Fail = new Fail(message, sarcasm).save()
    flash.success("Thanks for adding a new fail")
    index()
  }

  def captcha(id: String) {
    val captcha = Images.captcha()
    val code = captcha.getText("#000000")
    Cache.set(id, code, "10mn")
    renderBinary(captcha)
  }

  def plus(failId: Long, currentOffset: Int) = {
    val fail: Fail = Fail.findById(failId)
    fail.addPlusVote(session.get("user"))
    index(currentOffset)
  }

  def minus(failId: Long, currentOffset: Int) = {
    val fail: Fail = Fail.findById(failId)
    fail.addMinusVote(session.get("user"))
    index(currentOffset)
  }

}
