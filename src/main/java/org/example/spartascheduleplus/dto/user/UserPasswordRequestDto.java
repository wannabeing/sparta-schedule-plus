package org.example.spartascheduleplus.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false) // 존재하지 않는 필드가 들어오면 무시 false
@Getter
public class UserPasswordRequestDto {
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    @Size(max = 20, message = "현재 비밀번호는 20자 이내로 입력해주세요.")
    private final String currentPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    @Size(max = 20, message = "새로운 비밀번호는 20자 이내로 입력해주세요.")
    private final String newPassword;
}
