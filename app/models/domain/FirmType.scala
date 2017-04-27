package models.domain

case class FirmType(
  `type`: String,
  optId: Option[Int] = None
) {
  lazy val id = optId.getOrElse(-1)
}
