package models.domain

import play.api.libs.json._

case class Province(name: String, idRegion: Int, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
  def toJson: JsObject = Province.Implicits.provinceJsonWrites.writes(this).as[JsObject]
}

object Province {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val provinceJsonWrites = new Writes[Province] {
      def writes(p: Province): JsValue = Json.obj(
        "name" -> p.name,
        "idRegion" -> p.idRegion,
        "id" -> p.id
      )
    }
  }
}
