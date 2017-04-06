package models.domain

import play.api.libs.json._

case class Municipality(name: String, areacode: Int, idProvince: Int, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
  def toJson: JsObject = Municipality.Implicits.municipalityJsonWrites.writes(this).as[JsObject]
}

object Municipality {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val municipalityJsonWrites = new Writes[Municipality] {
      def writes(m: Municipality): JsValue = Json.obj(
        "name" -> m.name,
        "areacode" -> m.areacode,
        "idProvince" -> m.idProvince,
        "id" -> m.id
      )
    }
  }
}
