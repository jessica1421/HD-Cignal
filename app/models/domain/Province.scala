package models.domain

case class Province(name: String, idRegion: Int, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
}
