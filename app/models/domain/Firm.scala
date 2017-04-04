®package models.domain

case class Firm(
    name: String,
    address: String,
    postingPeriod: Short,
    accountingPeriodCode: Short,
    `type`: String,
    idBusinessType: Int, // FK
    idVat: Int, //FK
    areaCode: Int, //FK
    codeAccountingMethod: String, //FK
    codePostingMethod: String, //FK
    idParent: Option[Int], //FK
    optId: Option[Int] = None) {
  lazy val id: Int = optId.getOrElse(-1)
  lazy val accountingPeriod: String = if (accountingPeriod == 1) "FISCAL" else "CALENDAR"
}



// Chart of Account

// Accounting Method
 // -"ACCRUAL"
 // -"CASH"
 // -"HYBRID"

// Posting Method

// Accounting Period
// codeAccountingMethod
// codePostingMethod
