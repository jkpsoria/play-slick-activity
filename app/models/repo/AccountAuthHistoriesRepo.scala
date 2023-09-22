package models.repo

import scala.concurrent.ExecutionContext
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.domain._
import java.util.UUID
import scala.concurrent.Future
import java.time.LocalDateTime

@Singleton
final class AccountAuthHistoriesRepo @Inject()
(protected val dbConfigProvider: DatabaseConfigProvider)
(implicit ex: ExecutionContext) 
extends HasDatabaseConfigProvider[JdbcProfile] {
    import profile.api._

    class AccountAuthHistoriesTable(tag: Tag) extends Table[AccountAuthHistories](tag, "ACCOUNT") {
        def accountId = column[UUID]("ACCOUNT_ID")
        def isSuccess = column[Boolean]("IS_SUCCESS")
        def attemptedAt = column[LocalDateTime]("ATTEMPTED_AT")
        def pk = primaryKey("ACCOUNT_ID_ATTEMPTED_AT_PK", (accountId, attemptedAt))
        def * = (accountId, isSuccess, attemptedAt).mapTo[AccountAuthHistories]
    }

    val accAuthHistories = TableQuery[AccountAuthHistoriesTable]

    def createAccAuthHistories = db.run(accAuthHistories.schema.create)
    //2
    def insertAccAuthHistories(accAuthHistory: AccountAuthHistories) = db.run(accAuthHistories += accAuthHistory)


}