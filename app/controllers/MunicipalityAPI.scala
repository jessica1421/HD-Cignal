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
import models.service.MunicipalityService
import models.repo.MunicipalityRepo
import models.domain.Municipality
import errors._

@Singleton
class MunicipalityAPI @Inject() (
  val messagesApi: MessagesApi,
  val service: MunicipalityService,
  val repo: MunicipalityRepo,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def municipalityForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "areacode" -> number,
      "idProvince" -> number,
      "id" -> default(optional(number), None)
    )(Municipality.apply)(Municipality.unapply)
  )

  def getByCountry(idCountry: Int) = Action.async { implicit request =>
    import models.domain.ContactInfo.Implicits._
    service
      .getByCountry(idCountry)
      .map { r =>
        if(!r.isEmpty) {
          Ok(Json.toJson(r))
        } else {
          NotFound(Json.obj(
            "status" -> "failed",
            "message" -> "NotFound"))
        }
      }
  }

  def getByRegion(idRegion: Int) = Action.async { implicit request =>
    import models.domain.ContactInfo.Implicits._
    service
      .getByRegion(idRegion)
      .map { r =>
        if(!r.isEmpty) {
          Ok(Json.toJson(r))
        } else {
          NotFound(Json.obj(
            "status" -> "failed",
            "message" -> "NotFound"))
        }
      }
  }

  def getByProvince(idProvince: Int) = Action.async { implicit request =>
    import models.domain.ContactInfo.Implicits._
    service
      .getByProvince(idProvince)
      .map { r =>
        if(!r.isEmpty) {
          Ok(Json.toJson(r))
        } else {
          NotFound(Json.obj(
            "status" -> "failed",
            "message" -> "NotFound"))
        }
      }
  }

}
