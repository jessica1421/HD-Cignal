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
import models.service.ContactInfoService
import models.repo.ContactInfoRepo
import models.domain.ContactInfo
import errors._

@Singleton
class ContactInfoAPI @Inject() (
  val messagesApi: MessagesApi,
  val service: ContactInfoService,
  val repo: ContactInfoRepo,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def contactInfoForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "idCompany" -> number,
      "id" -> default(optional(number), None)
    )(ContactInfo.apply)(ContactInfo.unapply)
  )

  def add = Action.async { implicit requests =>
    contactInfoForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
        service
          .add(_)
          .map(ServiceErrorHandler.json(_))
          .getOrElse(Created)
    )
  }

  def update = Action.async { implicit requests =>
    contactInfoForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
        service
          .update(_)
          .map(ServiceErrorHandler.json(_))
          .getOrElse(Created)
    )
  }

  def delete = Action.async { implicit requests =>
    Form("id" -> number).bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
        service
          .delete(_)
          .map(ServiceErrorHandler.json(_))
          .getOrElse(Created)
    )
  }

  def getByFirm(idFirm: Int) = Action.async { implicit requests =>
    import models.domain.ContactInfo.Implicits._
    repo
      .getByFirm(idFirm)
      .map(r => Ok(Json.toJson(r)))
  }

  def all = Action.async { implicit requests =>
    import models.domain.ContactInfo.Implicits._
    repo.get.map(r => Ok(Json.toJson(r)))
  }

}
