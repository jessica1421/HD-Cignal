package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.i18n.{ I18nSupport, MessagesApi }

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class FirmAPI @Inject() (
  val messagesApi: MessagesApi
) extends Controller with I18nSupport {

  def add = TODO
  def edit = TODO
  def get = TODO
  def state = TODO //enable or disable

}
