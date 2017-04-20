package models.domain

import play.api.libs.json._

case class AccountClassification(name: String, isMain: Boolean, optId: Option[Int]) {
  def id: Int = optId.getOrElse(0)
  def toJson: JsObject = AccountClassification
    .Implicits
    .accountClassificicationJsonWrites
    .writes(this).as[JsObject]
}

object AccountClassification{
  val tupled = (apply _).tupled
  object Implicits {
    implicit val accountClassificicationJsonWrites = new Writes[AccountClassification] {
      def writes(accountClassificication:  AccountClassification): JsValue = Json.obj(
        "account_id" -> accountClassificication.id,
        "name" -> accountClassificication.name,
        "is_main" -> accountClassificication.isMain
      )
    }
  }
}


// Main Account
//   Assets
//   Liabilities
//   Capitalization/Equity
//   Income
//   Expense

// Subsidiary Account
//   Cash
//   Receivable
//   Current Assets
//   Other Current Assets
//   Fixed Assets
