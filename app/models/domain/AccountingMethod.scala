package models.domain

import play.api.libs.json._

case class AccountingMethod(code: String, name: String) {
  def toJson: JsObject = AccountingMethod.Implicits.accountingMethodJsonWrites.writes(this).as[JsObject]
}
// Accrual or CashBasis or Hybrid

object AccountingMethod {
  val tupled = (apply _).tupled
  object Implicits {
    implicit val accountingMethodJsonWrites = new Writes[AccountingMethod] {
      def writes(am:  AccountingMethod): JsValue = Json.obj(
        "code" -> am.code,
        "name" -> am.name
      )
    }
  }
}
