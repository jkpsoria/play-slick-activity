package models.domain

import java.util.UUID
import java.time.LocalDateTime


sealed trait Status
case object Active extends Status
case object Inactive extends Status
case object Disabled extends Status


case class Account(id: UUID, email: String, status: Status, createdAt: LocalDateTime)