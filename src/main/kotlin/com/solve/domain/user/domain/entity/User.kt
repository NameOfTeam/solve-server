package com.solve.domain.user.domain.entity

import com.solve.domain.user.domain.enums.UserRole
import com.solve.global.common.entity.BaseTimeEntity
import com.solve.global.common.enums.Tier
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import java.time.LocalDate
import java.util.*

@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_user_username", columnList = "username"),
        Index(name = "idx_user_email", columnList = "email"),
        Index(name = "idx_user_tier", columnList = "tier"),
        Index(name = "idx_user_role", columnList = "role")
    ]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "username", nullable = false, unique = true)
    var username: String,

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "introduction", columnDefinition = "TEXT")
    var introduction: String? = null,

    @Column(name = "is_verified", nullable = false)
    var isVerified: Boolean = false,

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val solved: MutableSet<UserSolved> = mutableSetOf(),

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: UserRole = UserRole.USER,

    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false)
    var tier: Tier = Tier.ROOKIE,

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val frozen: MutableSet<UserFrozen> = mutableSetOf(),

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val connections: MutableSet<UserConnection> = mutableSetOf()
) : BaseTimeEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    val streak: Int
        get() {
            if (solved.isEmpty() && frozen.isEmpty()) return 0

            val activeDates = (solved.map { it.date } + frozen.map { it.date })
                .distinct()
                .sortedDescending()

            if (activeDates.isEmpty() || activeDates.first() < LocalDate.now().minusDays(1)) {
                return 0
            }

            var currentStreak = 1
            var previousDate = activeDates.first()

            for (i in 1 until activeDates.size) {
                val currentDate = activeDates[i]
                if (previousDate.minusDays(1) == currentDate) {
                    currentStreak++
                    previousDate = currentDate
                } else {
                    break
                }
            }

            return currentStreak
        }

    val maxStreak: Int
        get() {
            if (solved.isEmpty() && frozen.isEmpty()) return 0

            val activeDates = (solved.map { it.date } + frozen.map { it.date })
                .distinct()
                .sorted()

            if (activeDates.isEmpty()) return 0

            var maxStreak = 1
            var currentStreak = 1
            var previousDate = activeDates.first()

            for (i in 1 until activeDates.size) {
                val currentDate = activeDates[i]
                if (previousDate.plusDays(1) == currentDate) {
                    currentStreak++
                    maxStreak = maxOf(maxStreak, currentStreak)
                } else {
                    currentStreak = 1
                }
                previousDate = currentDate
            }

            return maxStreak
        }

    val grass: Map<LocalDate, Int>
        get() {
            val registrationDate = this.createdAt.toLocalDate()
            val freezeDates = frozen.map { it.date }.toSet()
            val solvedCounts = solved
                .groupBy { it.date }
                .mapValues { it.value.size }

            val allDates = generateSequence(registrationDate) { date ->
                if (date < LocalDate.now()) date.plusDays(1) else null
            }.toList()

            return allDates.associateWith { date ->
                when {
                    freezeDates.contains(date) -> -1
                    solvedCounts.containsKey(date) -> solvedCounts[date]!!
                    else -> 0
                }
            }
        }

    val solvedToday: Boolean
        get() = solved.any { it.date == LocalDate.now() }

    val solvedCount: Int
        get() = solved.size
}