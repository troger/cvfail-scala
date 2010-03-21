package models

import java.util.Date
import play.db.jpa.{QueryOn, Model}
import javax.persistence.Entity

/**
 * @author <a href="mailto:troger@nuxeo.com">Thomas Roger</a>
 */

@Entity
class Fail(
  var message: String,
  var sarcasm: String
  ) extends Model {

  var postedAt = new Date()

  def this() = this (null, null)

}

object Fail extends QueryOn[Fail] {
}
