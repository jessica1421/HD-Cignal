package errors

import play.api.mvc._

object ServiceErrorHandler {

  def json(error: Error): Result = error match {
    case UnknownError => Results.InternalServerError

    case SubjectConflicts => Results.Conflict
    case SubjectNotExists => Results.NotFound
    case ObjectConflicts => Results.Conflict
    case ObjectNotExists => Results.NotFound

    case _ => json(UnknownError)
  }
}
