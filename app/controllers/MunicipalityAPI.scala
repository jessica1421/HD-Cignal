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

  def getByCountry = Action.async { implicit request =>
    import models.domain.ContactInfo.Implicits._
    Form("idCountry" -> number).bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)), {
        service
          .getByCountry(_)
          .map(r => Ok(Json.obj("datas" -> r)))
      })}

  def getByRegion = Action.async { implicit request =>
    import models.domain.ContactInfo.Implicits._
    Form("idRegion" -> number).bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)), {
        service
          .getByRegion(_)
          .map(r => Ok(Json.obj("datas" -> r)))
      })}

  def getByProvince = Action.async { implicit request =>
    import models.domain.ContactInfo.Implicits._
    Form("idProvince" -> number).bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.errorsAsJson)), {
        service
          .getByProvince(_)
          .map(r => Ok(Json.obj("datas" -> r)))
      })}

}
