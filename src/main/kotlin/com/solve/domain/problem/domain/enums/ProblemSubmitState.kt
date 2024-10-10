package com.solve.domain.problem.domain.enums

enum class ProblemSubmitState {
    // 맞았습니다
    ACCEPTED,

    // 틀렸습니다
    WRONG_ANSWER,

    // 출력 형식이 잘못되었습니다
    PRESENTATION_ERROR,

    // 시간 초과
    TIME_LIMIT_EXCEEDED,

    // 메모리 초과
    MEMORY_LIMIT_EXCEEDED,

    // 런타임 에러
    RUNTIME_ERROR,

    // 기다리는 중
    PENDING,

    // 채점 준비 중
    JUDGING,

    // 채점 중
    JUDGING_IN_PROGRESS,
}