package org.example.spartascheduleplus.dto.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [API 응답 DTO]
 * status: 상태
 * message: 메시지
 */
@RequiredArgsConstructor
@Getter
public class ApiResponseDto {
    private final String status;
    private final String message;
}

