package models.queries


import scala.concurrent.ExecutionContext
import javax.inject._
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile
import models.domain._
import java.util.UUID
import scala.concurrent.Future
import java.time.LocalDateTime
import models.repo._

@Singleton
final class Queries @Inject()
(protected val dbConfigProvider: DatabaseConfigProvider, val profileRepo: ProfileRepo, val accountRepo: AccountRepo, val accAuthHistories: AccountAuthHistoriesRepo)
(implicit ex: ExecutionContext) 
extends HasDatabaseConfigProvider[JdbcProfile] {
    import profile.api._


    //2

    def insertAccount(account: Account) = db.run(accountRepo.accounts += account)

    //3
    def getAllAccountsProfilesT = {
        val query = for {
            profiles <- profileRepo.profiles
            account <- accountRepo.accounts if profiles.gender != "Male" && profiles.gender != "Female"
        } yield (account)
        db.run(query.result)
    }


    //4

    def updateProfileFirstLastNames(id: UUID, firstName: String, lastName: String): Future[Int] = 
        db.run(profileRepo.profiles.filter(_.accountID === id).map(x => (x.firstName, x.lastName)).update((firstName, lastName)))

    
    //5 
    def getProfileByFirstName(keyword: String) = 
        db.run(profileRepo.profiles.filter(_.firstName.like(s"$keyword")).result)

    //6
    def getAllAccountsCreatedBetween(start: LocalDateTime, end: LocalDateTime) = 
        db.run(accountRepo.accounts.filter(_.createdAt.between(start, end)).result)
    
    //7 
    
    def getDayWithMostAttempts = {
        val query = for {
            accAuthHistories <- accAuthHistories.accAuthHistories
        } yield accAuthHistories
        db.run(query.map(_.attemptedAt).max.result)
    }
    
}