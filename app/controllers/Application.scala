package controllers

import play.mvc._
import models.Fail

object Application extends Controller {

  def index = {
    val fails = Fail.find("order by postedAt desc").fetch
    render(fails)
  }

  def postFail() = {
    
  }

}
