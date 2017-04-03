package errors

class Error private (val code: Int, val reason: Option[String] = None) {
  def toThrowable: ThrowableError = new ThrowableError(this)

  override def toString: String =
    reason.map(r => s"Error($code): $r").getOrElse(s"Error($code)")
}

object Error {
  private[errors] def apply(code: Int, reason: Option[String] = None): Error =
    new Error(code, reason)

  private[errors] def apply(code: Int, reason: String): Error =
    new Error(code, Some(reason))

  def unapply(error: Error): Option[(Int, Option[String])] =
    Some((error.code, error.reason))

  def unapply(error: ThrowableError): Option[(Int, Option[String])] =
    Some((error.code, error.reason))
}
