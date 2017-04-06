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
import models.repo.AccountingPeriodRepo
import models.domain.AccountingPeriod
import errors._

@Singleton
class AccountingPeriodAPI @Inject() (
  val messagesApi: MessagesApi,
  val repo: AccountingPeriodRepo,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def all = Action.async { implicit requests =>
    import models.domain.AccountingPeriod.Implicits._
    repo.get.map(r => Ok(Json.obj("accountingPeriods" -> r)))
  }

}
