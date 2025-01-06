package com.solve.domain.admin.statistic.service

interface AdminStatisticService {
    fun getTotalUsers(): Long
    fun getTotalContests(): Long
    fun getTotalProblems(): Long
    fun getTotalSubmissions(): Long
    fun getTotalOngoingContests(): Long
}