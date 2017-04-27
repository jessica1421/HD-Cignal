package models.repo

import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }
import cats.data.OptionT

@Singleton
class PostingMethodRepo @Inject()(
    dao: models.dao.PostingMethodDAO,
    implicit val ec: ExecutionContext,
    protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[utils.db.PostgresDriver] {
  import driver.api._

  def exists(code: String): Future[Boolean] = db.run(dao.query(code).exists.result)

  def get: Future[Seq[String]] = db.run(dao.query.result)

  def find(code: String): OptionT[Future, String] =
    OptionT(db.run(dao.query(code).result.headOption))

  def add(postingMethod: String): Future[Int] =
    db.run((dao.query += postingMethod))

  def delete(code: String): Future[Int] =
    db.run(dao.query(code).delete)
}
