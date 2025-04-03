package org.example.spartascheduleplus.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@JsonIgnoreProperties // 존재하지 않는 필드가 들어오면 예외 발생 (default: false)
@Getter
public class LoginRequestDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Size(max = 100, message = "이메일은 100자 이내로 입력해주세요.")
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    private final String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(max = 20, message = "비밀번호는 20자 이내로 입력해주세요.")
    private final String password;
}
