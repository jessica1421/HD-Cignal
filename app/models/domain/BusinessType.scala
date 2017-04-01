package models.domain

case class BusinessType(name: String, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
}
