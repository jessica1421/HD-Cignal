package models.domain

import play.api.libs.json._

case class ContactInfo(value: String, idFirm: Int, optId: Option[Int] = None) {
  lazy val id: Int = optId.getOrElse(0)
  def toJson: JsObject = ContactInfo.Implicits.contactInfoJsonWrites.writes(this).as[JsObject]
}

object ContactInfo {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val contactInfoJsonWrites = new Writes[ContactInfo] {
      def writes(ci: ContactInfo): JsValue = Json.obj(
        "value" -> ci.value,
        "idFirm" -> ci.idFirm,
        "id" -> ci.id
      )
    }
  }
}
