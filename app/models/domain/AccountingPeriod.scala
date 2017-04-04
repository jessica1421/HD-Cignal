package models.domain

import java.time.Instant

case class AccountingPeriod(
    idFirm: Int,
    startDate: Instant,
    endDate: Instant,
    optId: Option[Int] = None) {
  lazy val id: Int = optId.getOrElse(-1)
}
// value for fiscla year
