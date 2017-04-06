package models.domain

import play.api.libs.json._

case class Country(name: String, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
  def toJson: JsObject = Country.Implicits.countryJsonWrites.writes(this).as[JsObject]
}

object Country {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val countryJsonWrites = new Writes[Country] {
      def writes(c: Country): JsValue = Json.obj(
        "name" -> c.name,
        "id" -> c.id
      )
    }
  }
}
