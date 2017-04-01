package models.dao

import java.util.UUID
import javax.inject._
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import models.domain.Vat

@Singleton
final private[models] class VatDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class VatTable(tag: Tag) extends Table[Vat](tag, "VATS") {
    def name = column[String]("NAME")
    def value = column[Double]("VALUE")
    def id = column[Int]("ID", O.PrimaryKey)

    def * = (name, value, id.?) <> (Vat.tupled, Vat.unapply)
  }

  private[models] object query extends TableQuery(new VatTable(_)) {
    @inline def apply(id: Int) = this.withFilter(_.id === id)
  }
}
