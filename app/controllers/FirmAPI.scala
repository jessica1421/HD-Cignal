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
import models.service.FirmService
import models.repo.FirmRepo
import models.domain.Firm
import errors._

@Singleton
class FirmAPI @Inject() (
  val messagesApi: MessagesApi,
  val service: FirmService,
  val repo: FirmRepo,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def firmForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "address" -> nonEmptyText,
      "postingPeriod" -> shortNumber,
      "accountingPeriodCode" -> shortNumber,
      "idBusinessType" -> number,
      "idVat" -> number,
      "areaCode" -> number,
      "codeAccountingMethod" -> nonEmptyText,
      "codePostingMethod" -> nonEmptyText,
      "idParent" -> optional(number),
      "idFirmType" -> number,
      "isEnable" -> boolean,
      "id" -> optional(number)
    )(Firm.apply)(Firm.unapply)
  )

  val firmWithContacts = Form(tuple("firm" -> firmForm.mapping, "contact_infos" -> seq(nonEmptyText)))

  def all = Action.async { implicit requests =>
    import models.domain.Firm.Implicits._
    repo.get.map(r => Ok(Json.toJson(r)))
  }

  def add = Action.async { implicit requests =>
    firmWithContacts.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
      { case (a, b) =>
        service
          .addFirm(a, b)
          .map(ServiceErrorHandler.json(_))
          .getOrElse(Created)
      }
    )
  }

  // def update = Action.async { implicit requests =>
  //   businessTypeForm.bindFromRequest.fold(
  //     formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
  //       service
  //         .update(_)
  //         .map(ServiceErrorHandler.json(_))
  //         .getOrElse(Created)
  //   )
  // }

  // def delete = Action.async { implicit requests =>
  //   Form("id" -> number).bindFromRequest.fold(
  //     formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
  //       service
  //         .delete(_)
  //         .map(ServiceErrorHandler.json(_))
  //         .getOrElse(Created)
  //   )
  // }

  // def find = Action.async { implicit requests =>
  //   import models.domain.BusinessType.Implicits._
  //   Form("id" -> number).bindFromRequest.fold(
  //     formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)), {
  //       repo
  //         .find(_)
  //         .map(r => Ok(r.toJson))
  //         .getOrElse(NotFound)

  //     }
  //   )
  // }

}
