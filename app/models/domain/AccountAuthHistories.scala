package models.domain

import java.util.UUID
import java.time.LocalDateTime




case class AccountAuthHistories(accountId: UUID, isSuccess: Boolean, attemptedAt: LocalDateTime)

