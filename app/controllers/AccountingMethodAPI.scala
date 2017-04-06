package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }

import scala.concurrent.{ ExecutionContext, Future }

import cats.implicits._
import models.repo.AccountingMethodRepo
import models.domain.AccountingMethod
import errors._

@Singleton
class AccountingMethodAPI @Inject() (
  val messagesApi: MessagesApi,
  val repo: AccountingMethodRepo,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def all = Action.async { implicit requests =>
    import models.domain.AccountingMethod.Implicits._
    repo.get.map(r => Ok(Json.obj("accountingMethods" -> r)))
  }

}
