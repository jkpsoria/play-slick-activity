package models.domain

import java.util.UUID
import java.time.LocalDateTime


sealed trait Gender
case object Male extends Gender
case object Female extends Gender
case object Transgender extends Gender


case class Profile(accountID: UUID, firstName: String, lastName: String, gender: Gender)