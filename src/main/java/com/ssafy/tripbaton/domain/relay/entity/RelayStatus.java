package com.ssafy.tripbaton.domain.relay.entity;

public enum RelayStatus {
    // 활성화 된 릴레이
    ACTIVE,

    STALE,

    // 배치에 의해 닫힌 릴레이
    ARCHIVED,

    // 참여 가능한 릴레이
    OPEN,

    // 누군가 참여 중인 릴레이
    RUNNING
}
