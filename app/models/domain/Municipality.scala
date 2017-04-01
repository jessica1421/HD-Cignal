package models.domain

case class Municipality(name: String, areacode: Int, idProvince: Int, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
}
