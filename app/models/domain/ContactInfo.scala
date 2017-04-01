package models.domain

case class ContactInfo(name: String, idCompany: Int, optId: Option[Int]) {
  lazy val id: Int = optId.getOrElse(0)
}
