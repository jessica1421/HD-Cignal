package models.domain

import play.api.libs.json._

case class Firm(
    name: String,
    address: String,
    postingPeriod: Short,
    accountingPeriodCode: Short,
    idBusinessType: Int, // FK
    idVat: Int, //FK
    areaCode: Int, //FK
    codeAccountingMethod: String, //FK
    codePostingMethod: String, //FK
    idParent: Option[Int], //FK
    idFirmType: Int, //FK
    isEnable: Boolean, //FK
    optId: Option[Int] = None) {
  lazy val id: Int = optId.getOrElse(-1)
  lazy val accountingPeriod: String = if (accountingPeriod == 1) "FISCAL" else "CALENDAR"

  def toJson: JsObject = Firm.Implicits.firmJsonWrites.writes(this).as[JsObject]
}

object Firm{
  val tupled = (apply _).tupled
  object Implicits {
    implicit val firmJsonWrites = new Writes[Firm] {
      def writes(firm:  Firm): JsValue = Json.obj(
        "name" -> firm.name,
        "address" -> firm.address,
        "postingPeriod" -> firm.postingPeriod,
        "idBusinessType" -> firm.idBusinessType,
        "idVat" -> firm.idVat,
        "areaCode" -> firm.areaCode,
        "codeAccountingMethod" -> firm.codeAccountingMethod,
        "codePostingMethod" -> firm.codePostingMethod,
        "idParent" -> firm.idParent,
        "idFirmType" -> firm.idFirmType,
        "isEnable" -> firm.isEnable,
        "id" -> firm.id)
    }
  }
}


// accountingMethod
// bussinessType
// vat
// postingMethod



// Chart of Account

// Accounting Method
 // -"ACCRUAL"
 // -"CASH"
 // -"HYBRID"

// Posting Method

// Accounting Period
// codeAccountingMethod
// codePostingMethod
