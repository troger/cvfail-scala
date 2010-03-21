package controllers

import play.mvc.Controller
import models.Fail
import play.data.validation.{Required, Validation}

/**
 * @author <a href="mailto:troger@nuxeo.com">Thomas Roger</a>
 */

object FailController extends Controller {

  def index = {
    val fails = Fail.find("order by postedAt desc").fetch
    render(fails)
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
    index
  }

}
