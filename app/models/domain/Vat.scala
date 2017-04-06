package models.domain

import play.api.libs.json._

case class Vat(name: String, value: Double, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
  def toJson: JsObject = Vat.Implicits.vatJsonWrites.writes(this).as[JsObject]
}

object Vat {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val vatJsonWrites = new Writes[Vat] {
      def writes(v: Vat): JsValue = Json.obj(
        "name" -> v.name,
        "value" -> v.value,
        "id" -> v.id
      )
    }
  }
}
