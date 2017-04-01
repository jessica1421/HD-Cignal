package models.dao

import java.util.UUID
import javax.inject._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.ContactInfo

@Singleton
final private[models] class ContactInfoDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class ContactInfoTable(tag: Tag) extends Table[ContactInfo](tag, "CONTACTINFOS") {
    def name = column[String]("NAME")
    def idCompany = column[Int]("ID_COMPANY")
    def id = column[Int]("ID", O.PrimaryKey)

    def * = (name, idCompany, id.?) <> (ContactInfo.tupled, ContactInfo.unapply)
  }

  private[models] object query extends TableQuery(new ContactInfoTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
