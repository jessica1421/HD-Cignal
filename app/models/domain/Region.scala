package models.domain

case class Region(name: String, idCountry: Int, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
}
