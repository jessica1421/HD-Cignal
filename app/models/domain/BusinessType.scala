package models.domain

import play.api.libs.json._

case class BusinessType(name: String, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
  def toJson: JsObject = BusinessType.Implicits.businessTypeJsonWrites.writes(this).as[JsObject]
}

object BusinessType {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val businessTypeJsonWrites = new Writes[BusinessType] {
      def writes(bt:  BusinessType): JsValue = Json.obj(
        "id" -> bt.id,
        "name" -> bt.name
      )
    }
  }
}
