package models.domain

import play.api.libs.json._

case class Region(name: String, idCountry: Int, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
  def toJson: JsObject = Region.Implicits.regionJsonWrites.writes(this).as[JsObject]
}

object Region {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val regionJsonWrites = new Writes[Region] {
      def writes(p: Region): JsValue = Json.obj(
        "name" -> p.name,
        "idCountry" -> p.idCountry,
        "id" -> p.id
      )
    }
  }
}
