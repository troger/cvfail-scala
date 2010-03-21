package controllers

import play.mvc._
import models.Fail
import play.data.validation.{Validation, Required}
import play.libs.OpenID
import play.libs.OpenID.UserInfo

object Application extends Controller {

  private val discoveryUrls = Map("google" -> "https://www.google.com/accounts/o8/id",
    "yahoo" -> "https://me.yahoo.com/")



  def login = {
    render()
  }

  def logout = {
    session.remove("user")
    FailController.index
  }

  def authenticate(provider: String): Unit = {
    if (OpenID.isAuthenticationResponse) {
      val verifiedUser = OpenID.getVerifiedID
        if(verifiedUser == null) {
            flash.put("error", "Authentication has failed")
            login
        }
        session.put("user", verifiedUser.id)
        FailController.create
    } else {
      OpenID.id(discoveryUrls(provider)).verify
    }
  }



}
