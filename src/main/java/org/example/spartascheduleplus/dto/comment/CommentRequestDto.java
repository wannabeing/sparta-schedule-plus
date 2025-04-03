package org.example.spartascheduleplus.dto.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonIgnoreProperties // 존재하지 않는 필드가 들어오면 예외 발생 (default: false)
@Getter
public class CommentRequestDto {
    @NotBlank(message = "댓글을 입력해주세요.")
    @Size(max = 200, message = "댓글은 200자 이내로 입력해주세요.")
    private final String comment;
}
