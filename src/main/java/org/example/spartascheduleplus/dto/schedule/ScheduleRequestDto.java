package org.example.spartascheduleplus.dto.schedule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonIgnoreProperties // 존재하지 않는 필드가 들어오면 예외 발생 (default: false)
@Getter
@RequiredArgsConstructor
public class ScheduleRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "제목은 50자 이내로 입력해주세요.")
    private final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 200, message = "내용은 200자 이내로 입력해주세요.")
    private final String contents;
}
