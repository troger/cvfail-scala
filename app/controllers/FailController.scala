package controllers

import models.Fail
import play.data.validation.{Required, Validation}
import play.mvc.{ScalaController, Controller}

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
    render(fails, offset.asInstanceOf[Object],
          oldest.asInstanceOf[Object], older.asInstanceOf[Object],
          newer.asInstanceOf[Object], showOlderLink.asInstanceOf[Object],
          showOldestLink.asInstanceOf[Object])
  }


  def create = {
    if (!session.contains("user")) {
      Application.login
    }
    render()
  }

  def postFail(@Required(message = "A message is required") message: String,
               sarcasm: String) = {
    if (Validation.hasErrors()) {
      render("FailController/create.html")
    }

    val newFail: Fail = new Fail(message, sarcasm).save()
    flash.success("Thanks for adding a new fail")
    index()
  }

}
