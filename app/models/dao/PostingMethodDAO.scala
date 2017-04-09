package models.dao

import javax.inject.{ Inject, Singleton }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }

@Singleton
final private[models] class PostingMethodDAO @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  protected class PostingMethodTable(tag: Tag)
    extends Table[String](tag, "POSTING_METHODS") {
    def code = column[String]("CODE", O.PrimaryKey)

    def * = code
  }

  private[models] object query extends TableQuery(new PostingMethodTable(_)) {
    @inline def apply(code: String) = this.withFilter(_.code === code)
  }
}
