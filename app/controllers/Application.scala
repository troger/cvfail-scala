package controllers

import play.mvc._
import models.Fail
import play.data.validation.Required

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
    val newFail: Fail = new Fail(message, sarcasm).save()
    index
  }

}
