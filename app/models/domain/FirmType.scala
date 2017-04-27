package models.domain

import play.api.libs.json._

case class FirmType(
  `type`: String,
  optId: Option[Int] = None
) {
  lazy val id = optId.getOrElse(-1)
  def toJson: JsObject = FirmType.Implicits.firmTypeJsonWrites.writes(this).as[JsObject]
}

object FirmType {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val firmTypeJsonWrites = new Writes[FirmType] {
      def writes(ft: FirmType): JsValue = Json.obj(
        "name" -> ft.`type`,
        "id" -> ft.id
      )
    }
  }
}
