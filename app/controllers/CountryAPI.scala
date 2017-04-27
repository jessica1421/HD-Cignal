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
import models.service.CountryService
import models.repo.CountryRepo
import models.domain.Country
import errors._

@Singleton
class CountryAPI @Inject() (
  val messagesApi: MessagesApi,
  val service: CountryService,
  val repo: CountryRepo,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def countryForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "id" -> default(optional(number), None)
    )(Country.apply)(Country.unapply)
  )
  //name: String, optId: Option[Int]

   def add = Action.async { implicit requests =>
    countryForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)),
        service
          .add(_)
          .map(ServiceErrorHandler.json(_))
          .getOrElse(Created)
    )
  }

  def update = Action.async { implicit requests =>
    countryForm.bindFromRequest.fold(
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

  def all = Action.async { implicit requests =>
    import models.domain.Country.Implicits._
    repo.get.map(r => Ok(Json.toJson(r)))
  }

  def find(id: Int) = Action.async { implicit requests =>
    import models.domain.Country.Implicits._
    repo.get.map(r => Ok(Json.toJson(r)))
    repo
      .find(id)
      .map(r => Ok(r.toJson))
      .getOrElse(NotFound)
  }

}
