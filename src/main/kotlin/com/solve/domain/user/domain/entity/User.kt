package com.solve.domain.user.domain.entity

import com.solve.domain.user.domain.enums.UserRank
import com.solve.domain.user.domain.enums.UserRole
import com.solve.global.common.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "users")
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

    @Column(name = "introduction")
    var introduction: String? = null,

    @Column(name = "verified", nullable = false)
    var verified: Boolean = false,

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val solved: MutableList<UserSolved> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    var role: UserRole = UserRole.USER,

    @Enumerated(EnumType.STRING)
    @Column(name = "rank", nullable = false)
    var rank: UserRank = UserRank.ROOKIE,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val connections: MutableList<UserConnection> = mutableListOf()
) : BaseTimeEntity() {
    val streak: Int
        get() {
            if (solved.isEmpty()) return 0

            val sortedDates = solved
                .map { it.date }
                .distinct()
                .sortedDescending()

            var currentStreak = 1
            var previousDate = sortedDates[0]

            if (previousDate.isBefore(LocalDate.now().minusDays(1))) {
                return 0
            }

            for (i in 1 until sortedDates.size) {
                val currentDate = sortedDates[i]

                if (previousDate.minusDays(1) != currentDate) {
                    break
                }

                currentStreak++
                previousDate = currentDate
            }

            return currentStreak
        }

    val maxStreak: Int
        get() {
            if (solved.isEmpty()) return 0

            val sortedDates = solved
                .map { it.date }
                .distinct()
                .sorted()

            var maxStreak = 1
            var currentStreak = 1
            var previousDate = sortedDates[0]

            for (i in 1 until sortedDates.size) {
                val currentDate = sortedDates[i]

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
        get() = solved
            .groupBy { it.date }
            .mapValues { it.value.size }
            .toMap()

    val solvedToday: Boolean
        get() = solved.any { it.date == LocalDate.now() }

    val solvedCount: Int
        get() = solved.size
}