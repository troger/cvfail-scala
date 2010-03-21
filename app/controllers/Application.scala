package controllers

import play.mvc._
import models.Fail
import play.data.validation.{Validation, Required}

object Application extends Controller {

  def index = {
    val fails = Fail.find("order by postedAt desc").fetch
    render(fails)
  }

  def create = {
    render()
  }

  def postFail(@Required(message = "A message is required") message: String,
               sarcasm: String) = {
    if (Validation.hasErrors()) {
      render("Application/create.html")
    }

    val newFail: Fail = new Fail(message, sarcasm).save()
    flash.success("Thanks for adding a new fail")
    index
  }

}
