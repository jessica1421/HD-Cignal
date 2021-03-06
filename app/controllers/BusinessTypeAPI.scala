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
import models.service.BusinessTypeService
import models.repo.BusinessTypeRepo
import models.domain.BusinessType
import errors._

@Singleton
class BusinessTypeAPI @Inject() (
  val messagesApi: MessagesApi,
  val service: BusinessTypeService,
  val repo: BusinessTypeRepo,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def businessTypeForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "id" -> default(optional(number), None)
    )(BusinessType.apply)(BusinessType.unapply)
  )

  def add = Action.async { implicit requests =>
    businessTypeForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
        service
          .add(_)
          .map(ServiceErrorHandler.json(_))
          .getOrElse(Created)
    )
  }

  def update = Action.async { implicit requests =>
    businessTypeForm.bindFromRequest.fold(
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

  def find(id: Int) = Action.async { implicit requests =>
    import models.domain.BusinessType.Implicits._
    repo
      .find(id)
      .map(r => Ok(r.toJson))
      .getOrElse(NotFound(Json.obj(
        "status" -> "failed",
        "message" -> "NotFound")))
  }

  def all = Action.async { implicit requests =>
    import models.domain.BusinessType.Implicits._
    repo.get.map(r => Ok(Json.toJson(r)))
  }

}
