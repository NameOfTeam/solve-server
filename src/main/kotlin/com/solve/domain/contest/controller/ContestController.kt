package com.solve.domain.contest.controller

import com.solve.domain.contest.domain.enums.ContestSearchState
import com.solve.domain.contest.dto.request.ContestProblemAddRequest
import com.solve.domain.contest.dto.request.CreateContestAnnouncementRequest
import com.solve.domain.contest.dto.request.UpdateContestAnnouncementRequest
import com.solve.domain.contest.service.ContestService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@Tag(name = "대회", description = "Contest")
@RestController
@RequestMapping("/contests")
class ContestController(
    private val contestService: ContestService
) {
    @Operation(summary = "대회 목록 조회", description = "대회 목록을 조회합니다.")
    @GetMapping
    fun getContests(@PageableDefault pageable: Pageable) = BaseResponse.of(contestService.getContests(pageable))

    @Operation(summary = "대회 상세 조회", description = "대회 상세를 조회합니다.")
    @GetMapping("/{contestId}")
    fun getContest(@PathVariable contestId: Long) = BaseResponse.of(contestService.getContest(contestId))

    @Operation(summary = "대회 검색")
    @GetMapping("/search")
    fun searchContest(
        @RequestParam(required = false, defaultValue = "") query: String,
        @RequestParam(required = false) state: ContestSearchState?,
        @PageableDefault pageable: Pageable
    ) = BaseResponse.of(contestService.searchContest(query, state, pageable))

    @GetMapping("/{contestId}/announcements")
    fun getContestAnnouncements(@PathVariable contestId: Long, @PageableDefault pageable: Pageable) =
        BaseResponse.of(contestService.getContestAnnouncements(contestId, pageable))

    @GetMapping("/{contestId}/announcements/{announcementId}")
    fun getContestAnnouncement(@PathVariable contestId: Long, @PathVariable announcementId: Long) =
        BaseResponse.of(contestService.getContestAnnouncement(contestId, announcementId))

    @PostMapping("/{contestId}/announcements")
    fun createContestAnnouncement(@PathVariable contestId: Long, @RequestBody request: CreateContestAnnouncementRequest) =
        BaseResponse.of(contestService.createContestAnnouncement(contestId, request), 201)

    @PatchMapping("/{contestId}/announcements/{announcementId}")
    fun updateContestAnnouncement(
        @PathVariable contestId: Long,
        @PathVariable announcementId: Long,
        @RequestBody request: UpdateContestAnnouncementRequest
    ) = BaseResponse.of(contestService.updateContestAnnouncement(contestId, announcementId, request))

    @DeleteMapping("/{contestId}/announcements/{announcementId}")
    fun deleteContestAnnouncement(@PathVariable contestId: Long, @PathVariable announcementId: Long) =
        BaseResponse.of(contestService.deleteContestAnnouncement(contestId, announcementId))

    @Operation(summary = "대회 문제 추가")
    @PostMapping("/{contestId}/problems")
    fun addContestToProblem(@PathVariable contestId: Long, @RequestBody request: ContestProblemAddRequest) =
        BaseResponse.of(contestService.addContestToProblem(contestId, request))

    @Operation(summary = "대회 문제 삭제")
    @DeleteMapping("/{contestId}/problems/{problemId}")
    fun removeContestFromProblem(@PathVariable contestId: Long, @PathVariable problemId: Long) =
        BaseResponse.of(contestService.removeContestFromProblem(contestId, problemId))


    @GetMapping("/{contestId}/participants")
    fun getContestParticipants(@PathVariable contestId: Long) =
        BaseResponse.of(contestService.getContestParticipants(contestId))

    @DeleteMapping("/{contestId}/participants/{participantId}")
    fun removeContestParticipant(@PathVariable contestId: Long, @PathVariable participantId: Long) =
        BaseResponse.of(contestService.removeContestParticipant(contestId, participantId))

    @Operation(summary = "대회 참가")
    @PostMapping("/{contestId}/register")
    fun registerContest(@PathVariable contestId: Long) =
        BaseResponse.of(contestService.registerContest(contestId), 201)

    @GetMapping("/{contestId}/leaderboard")
    fun getContestLeaderboard(@PathVariable contestId: Long) =
        BaseResponse.of(contestService.getContestLeaderboard(contestId))

    @GetMapping("/{contestId}/leaderboard/export")
    fun exportContestLeaderboard(@PathVariable contestId: Long) =
        BaseResponse.of(contestService.exportContestLeaderboard(contestId))
}