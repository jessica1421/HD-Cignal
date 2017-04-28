package models.domain

import java.time.Instant
import play.api.libs.json._

case class AccountingPeriod(
    idFirm: Int,
    startDate: Instant,
    endDate: Instant,
    optId: Option[Int] = None ) {
  lazy val id: Int  = optId.getOrElse(-1)
  def toJson: JsObject = AccountingPeriod.Implicits.accountingPeriodJsonWrites.writes(this).as[JsObject]
}

/**
  FISCAL: JAN - DEC
  CALENDAR: at least 12 mos
*/

object AccountingPeriod {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val accountingPeriodJsonWrites = new Writes[AccountingPeriod] {
      def writes(ap: AccountingPeriod): JsValue = Json.obj(
        "idFirm" -> ap.idFirm,
        "startDate" -> ap.startDate,
        "endDate" -> ap.endDate
      )
    }
  }
}


