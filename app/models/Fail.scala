package models

import play.db.jpa.{QueryOn, Model}
import java.util.{List, ArrayList, Date}
import javax.persistence.{JoinTable, OneToMany, Entity}

import scala.collection.JavaConversions._

/**
 * @author <a href="mailto:troger@nuxeo.com">Thomas Roger</a>
 */

@Entity
class Fail(
  var message: String,
  var sarcasm: String,
  @OneToMany
  @JoinTable(name="PLUS_VOTES")
  var plusVotes: List[Vote] = new ArrayList[Vote],
  @OneToMany
  @JoinTable(name="MINUS_VOTES")
  var minusVotes: List[Vote] = new ArrayList[Vote]
  ) extends Model {

  var postedAt = new Date()

  def this() = this(null, null, null, null)

  def addPlusVote(userId: String) = {
    val vote = new Vote(userId).save
    plusVotes.add(vote)
  }

  def addMinusVote(userId: String) = {
    val vote = new Vote(userId).save
    minusVotes.add(vote)
  }

  def canVote(userId: String) = {
    !asBuffer(plusVotes).exists(v => v.userId == userId) &&
      !asBuffer(minusVotes).exists(v => v.userId == userId)
  }

}

object Fail extends QueryOn[Fail] {
}
