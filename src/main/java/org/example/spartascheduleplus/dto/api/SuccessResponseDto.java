package org.example.spartascheduleplus.dto.api;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class SuccessResponseDto<T> {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    private final int code;
    private final String status;
    private final String path;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL) // ✅ null 이면 응답 JSON 에서 생략됨
    private final T data;
}
