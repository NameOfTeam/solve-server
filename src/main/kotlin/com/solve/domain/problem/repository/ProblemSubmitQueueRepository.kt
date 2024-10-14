package com.solve.domain.problem.repository

interface ProblemSubmitQueueRepository {
    fun push(submitId: Long)
    fun pop(): Long?
    fun size(): Int
}