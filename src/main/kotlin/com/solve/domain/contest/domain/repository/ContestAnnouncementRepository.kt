package com.solve.domain.contest.domain.repository

import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.entity.ContestAnnouncement
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ContestAnnouncementRepository: JpaRepository<ContestAnnouncement, Long> {
    fun findAllByContest(contest: Contest, pageable: Pageable): Page<ContestAnnouncement>
    fun findAllByContestOrderByImportantDescPublishedAtDesc(contest: Contest): List<ContestAnnouncement>
    fun findByIdAndContest(id: Long, contest: Contest): ContestAnnouncement?
    fun countByContest(contest: Contest): Long
}