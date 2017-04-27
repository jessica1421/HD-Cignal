package models.dao

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.ContactInfo

@Singleton
final private[models] class ContactInfoDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class ContactInfoTable(tag: Tag) extends Table[ContactInfo](tag, "CONTACTINFOS") {
    def value = column[String]("VALUE")
    def idFirm = column[Int]("ID_FIRM")
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)

    def * = (value, idFirm, id.?) <> (ContactInfo.tupled, ContactInfo.unapply)
  }

  private[models] object query extends TableQuery(new ContactInfoTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
