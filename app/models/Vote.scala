package models

import javax.persistence.Entity
import play.db.jpa.{QueryOn, Model}

/**
 * @author <a href="mailto:troger@nuxeo.com">Thomas Roger</a>
 */
@Entity
class Vote(
  var userId: String
  ) extends Model {

  def this() = this (null)
  
}

object Vote extends QueryOn[Vote] {

}
