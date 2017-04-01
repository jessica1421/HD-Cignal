package models.domain

case class Vat(name: String, value: Double, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
}
