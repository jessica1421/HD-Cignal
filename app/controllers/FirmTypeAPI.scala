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
import models.service.FirmTypeService
import models.repo.FirmTypeRepo
import models.domain.FirmType
import errors._

@Singleton
class FirmTypeAPI @Inject() (
  val messagesApi: MessagesApi,
  val service: FirmTypeService,
  val repo: FirmTypeRepo,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def firmForm = Form(
    mapping(
      "type" -> nonEmptyText,
      "id" -> optional(number)
    )(FirmType.apply)(FirmType.unapply)
  )

  def all = Action.async { implicit requests =>
    import models.domain.FirmType.Implicits._
    repo.get.map(r => Ok(Json.toJson(r)))
  }

  def update = Action.async { implicit requests =>
    firmForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
        service
          .update(_)
          .map(ServiceErrorHandler.json(_))
          .getOrElse(Created)
    )
  }

  // def delete = Action.async { implicit requests =>
  //   Form("id" -> number).bindFromRequest.fold(
  //     formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
  //       service
  //         .delete(_)
  //         .map(ServiceErrorHandler.json(_))
  //         .getOrElse(Created)
  //   )
  // }

  def find(id: Int) = Action.async { implicit requests =>
    import models.domain.FirmType.Implicits._
    repo.get.map(r => Ok(Json.toJson(r)))
    repo
      .find(id)
      .map(r => Ok(r.toJson))
      .getOrElse(NotFound(Json.obj(
        "status" -> "failed",
        "message" -> "NotFound")))
  }

}
