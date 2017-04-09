package models.domain

import play.api.libs.json._

case class Proprietor(name: String, share: Double, idFirm: Int, optId: Option[Int] = None) {
  def id: Int = optId.getOrElse(0)
  def toJson: JsObject = Proprietor.Implicits.proprietorJsonWrites.writes(this).as[JsObject]
}

object Proprietor {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val proprietorJsonWrites = new Writes[Proprietor] {
      def writes(proprietor:  Proprietor): JsValue = Json.obj(
        "id" -> proprietor.id,
        "firm_id" -> proprietor.idFirm,
        "share" -> proprietor.share,
        "name" -> proprietor.name
      )
    }
  }
}
