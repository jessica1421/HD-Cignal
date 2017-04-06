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
import models.repo.RegionRepo
import models.domain.Region
import errors._

@Singleton
class RegionAPI @Inject() (
  val messagesApi: MessagesApi,
  val repo: RegionRepo,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def all = Action.async { implicit requests =>
    import models.domain.Region.Implicits._
    repo.get.map(r => Ok(Json.obj("regions" -> r)))
  }

}
