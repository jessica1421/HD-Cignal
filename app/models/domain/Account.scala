package models.domain

import play.api.libs.json._

case class Account(
    postingCode: String,
    recordCode: String,
    chartCode: String,
    name: String,
    main: Int,
    subsidiary: Int,
    isNominal: Boolean,
    isDebit: Boolean,
    notes: String,
    remarks: String,
    idParent: Int,
    idFirm: Option[Int],
    optId: Option[Int]) {
  def id: Int = optId.getOrElse(0)

  def toJson: JsObject = Account.Implicits.accountJsonWrites.writes(this).as[JsObject]
}

object Account{
  val tupled = (apply _).tupled
  object Implicits {
    implicit val accountJsonWrites = new Writes[Account] {
      def writes(account:  Account): JsValue = Json.obj(
        "posting_code" -> account.postingCode,
        "record_code" -> account.recordCode,
        "chart_code" -> account.chartCode,
        "name" -> account.name,
        "main" -> account.main,
        "subsidiary" -> account.subsidiary,
        "is_nominal" -> account.isNominal,
        "is_debit" -> account.isDebit,
        "notes" -> account.notes,
        "remarks" -> account.remarks,
        "parent_id" -> account.idParent,
        "firm_id" -> account.idFirm,
        "account_id" -> account.id)
    }
  }
}

// Posting Code
// Record Code
// Chart Code
// Account Name
// Main Account  // fk to accountClassification where isMain is true
// Subsidiary Account // fk to accountClassification where isMain is false
// isNominal //Account Type
// isDebit // NormalBalance
// Notes
// Remarks
// idParent
