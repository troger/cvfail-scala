package controllers

import play.mvc._
import models.Fail
import play.data.validation.{Validation, Required}
import play.libs.OpenID
import play.libs.OpenID.UserInfo

object Application extends Controller {

  private val discoveryUrls = Map("google" -> "https://www.google.com/accounts/o8/id",
    "yahoo" -> "https://me.yahoo.com/")

  def index = {
    val fails = Fail.find("order by postedAt desc").fetch
    render(fails)
  }

  def create = {
    if(!session.contains("user")) {
      login
    }
    render()
  }

  def login = {
    render()
  }

  def logout = {
    session.remove("user")
    index
  }

  def authenticate(provider: String): Unit = {
    if (OpenID.isAuthenticationResponse) {
      val verifiedUser = OpenID.getVerifiedID
        if(verifiedUser == null) {
            flash.put("error", "Authentication has failed")
            login
        }
        session.put("user", verifiedUser.id)
        create
    } else {
      OpenID.id(discoveryUrls(provider)).verify
    }
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
