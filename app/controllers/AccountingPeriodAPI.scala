package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import java.time.Instant

import org.joda.time.{ DateTime, LocalDate }
import scala.concurrent.{ ExecutionContext, Future }

import cats.implicits._
import models.service.AccountingPeriodService
import models.repo.AccountingPeriodRepo
import models.domain.AccountingPeriod
import errors._

@Singleton
class AccountingPeriodAPI @Inject() (
  val messagesApi: MessagesApi,
  val repo: AccountingPeriodRepo,
  val service: AccountingPeriodService,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def accountingPeriodForm = Form(
    tuple(
      "idFirm" -> number,
      "startDate" -> longNumber,
      "endDate" -> longNumber,
      "id" -> optional(number)
    )
  )

  def all = Action.async { implicit requests =>
    import models.domain.AccountingPeriod.Implicits._
    repo.get.map(r => Ok(Json.toJson(r)))
  }

  def update = Action.async { implicit requests =>
    import models.domain.AccountingPeriod.Implicits._
    accountingPeriodForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
      { case (idFirm, startDate, endDate, id) =>
        /**
          Mic
            Check daw if pwd ba ni.. Long to Instant
        */
        val start = Instant.ofEpochMilli(startDate)
        val end = Instant.ofEpochMilli(endDate)
        service
          .update(AccountingPeriod(idFirm, start, end, id))
          .map(ServiceErrorHandler.json(_))
          .getOrElse(Created)
      }
    )
  }
}
