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
import models.repo.VatRepo
import models.domain.Vat
import errors._

@Singleton
class VatAPI @Inject() (
  val messagesApi: MessagesApi,
  val repo: VatRepo,
  implicit val ec: ExecutionContext
) extends Controller with I18nSupport {

  def all = Action.async { implicit requests =>
    import models.domain.Vat.Implicits._
    repo.get.map(r => Ok(Json.obj("vats" -> r)))
  }

}
