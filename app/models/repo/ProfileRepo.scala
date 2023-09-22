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
final class ProfileRepo @Inject()
(protected val dbConfigProvider: DatabaseConfigProvider)
(implicit ex: ExecutionContext, val accountRepo: AccountRepo) 
extends HasDatabaseConfigProvider[JdbcProfile] {
    import profile.api._


    implicit val genderColumnType: BaseColumnType[Gender] = MappedColumnType.base[Gender, String](
        gender => gender.toString,
            gend => gend match {
                case "Male" => Male
                case "Female" => Female
                case "Transgender" => Transgender
                case _ => throw new IllegalArgumentException(s"Invalid Gender")
        }
    )

    class ProfileTable(tag: Tag) extends Table[Profile](tag, "ACCOUNT") {
       def accountID = column[UUID]("ACCOUNT_ID", O.PrimaryKey)
       def firstName = column[String]("FIRST_NAME", O.Length(255))
       def lastName = column[String]("LAST_NAME", O.Length(255))
       def gender = column[Gender]("GENDER")

       def profileFK = foreignKey("PROFILE_FK", accountID, accountRepo.accounts)(_.id, onDelete = ForeignKeyAction.Cascade)
       def * = (accountID, firstName, lastName, gender).mapTo[Profile]
    }

    val profiles = TableQuery[ProfileTable]


    def createProfile = db.run(profiles.schema.create)
    //2
    def insertProfile(profile: Profile) = db.run(profiles += profile)
    


}

