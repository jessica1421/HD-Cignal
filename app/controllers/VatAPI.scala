package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.data.format.Formats._

import scala.concurrent.{ ExecutionContext, Future }

import cats.implicits._
import models.service.VatService
import models.repo.VatRepo
import models.domain.Vat
import errors._

@Singleton
class VatAPI @Inject() (
  val messagesApi: MessagesApi,
  val repo: VatRepo,
  val service: VatService,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def vatForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "value" -> of(doubleFormat),
      "id" -> default(optional(number), None)
    )(Vat.apply)(Vat.unapply)
  )

  def all = Action.async { implicit requests =>
    import models.domain.Vat.Implicits._
    repo.get.map(r => Ok(Json.toJson(r)))
  }

  def find(id: Int) = Action.async { implicit requests =>
    import models.domain.Vat.Implicits._
    repo
      .find(id)
      .map(r => Ok(r.toJson))
      .getOrElse(NotFound(Json.obj(
        "status" -> "failed",
        "message" -> "NotFound")))
  }

  def add = Action.async { implicit requests =>
    vatForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
        service
          .add(_)
          .map(ServiceErrorHandler.json(_))
          .getOrElse(Created)
    )
  }

  def update = Action.async { implicit requests =>
    vatForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
        service
          .update(_)
          .map(ServiceErrorHandler.json(_))
          .getOrElse(Created)
    )
  }
}
