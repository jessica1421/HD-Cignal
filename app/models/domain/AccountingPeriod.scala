package models.domain

import java.time.Instant
import play.api.libs.json._

case class AccountingPeriod(
    idFirm: Int,
    startDate: Instant,
    endDate: Instant) {
  def toJson: JsObject = AccountingPeriod.Implicits.accountingPeriodJsonWrites.writes(this).as[JsObject]
}
// value for fiscla year

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
