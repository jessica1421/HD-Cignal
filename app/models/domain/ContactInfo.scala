package models.domain

import play.api.libs.json._

case class ContactInfo(name: String, idCompany: Int, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
  def toJson: JsObject = ContactInfo.Implicits.contactInfoJsonWrites.writes(this).as[JsObject]
}

object ContactInfo {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val contactInfoJsonWrites = new Writes[ContactInfo] {
      def writes(ci: ContactInfo): JsValue = Json.obj(
        "name" -> ci.name,
        "idCompany" -> ci.idCompany,
        "id" -> ci.id
      )
    }
  }
}
