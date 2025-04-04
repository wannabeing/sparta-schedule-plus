package org.example.spartascheduleplus.dto.api;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

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


    /**
     * 🚀 성공응답 DTO 객체를 생성하는 메서드
     * @param status httpStatus
     * @param path 요청 URI
     * @param message 성공 메시지
     * @param data 데이터
     * @return 성공응답 DTO 객체를 반환
     */
    public static <T> SuccessResponseDto<T> createSuccessResponseDto(
            HttpStatus status,
            String path,
            String message,
            T data
    )
    {
        return new SuccessResponseDto<>(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                path,
                message,
                data
        );
    }
}
