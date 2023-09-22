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
import models.domain._

@Singleton
final class AccountRepo @Inject()
(protected val dbConfigProvider: DatabaseConfigProvider, val profileRepo: ProfileRepo)
(implicit ex: ExecutionContext) 
extends HasDatabaseConfigProvider[JdbcProfile] {
    import profile.api._

    // implicit val statusColumnType: BaseColumnType[Status] = MappedColumnType.base[Status, String](
    // status => status.toString,
    //     stats => stats match {
    //         case "Active" => Active
    //         case "Inactive" => Inactive
    //         case "Disabled" => Disabled
    //         case _ => throw new IllegalArgumentException(s"Invalid Status")
    //     }
    // )

    implicit val statusType =
        MappedColumnType.base[Status, String](
            status => status match {
            case Active => "Active"
            case Inactive => "Inactive"
            case Disabled      => "Disabled"
            },
            stats => stats match {
            case "Active" => Active
            case "Inactive" => Inactive
            case "Disabled" => Disabled
    })


    class AccountTable(tag: Tag) extends Table[Account](tag, "ACCOUNT") {
        def id = column[UUID]("ID", O.PrimaryKey)
        def email = column[String]("EMAIL", O.Length(255))       
        def status = column[Status]("STATUS")
        def createdAt = column[LocalDateTime]("CREATED_AT")
        def * = (id, email, status, createdAt).mapTo[Account]
    }

    val accounts = TableQuery[AccountTable]

    //Create Scala methods using Slick to insert data in each table.

    def createAccount = db.run(accounts.schema.create)
    //2




    
}
