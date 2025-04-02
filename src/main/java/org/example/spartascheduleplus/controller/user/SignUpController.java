package org.example.spartascheduleplus.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.api.SuccessResponseDto;
import org.example.spartascheduleplus.dto.user.SignUpRequestDto;
import org.example.spartascheduleplus.dto.user.UserResponseDto;
import org.example.spartascheduleplus.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    /**
     * [Controller] 유저를 생성하는 메서드
     * @param dto 입력받은 유저요청 객체
     * @param httpRequest Http 요청 객체
     * @return 생성된 유저응답 객체 반환
     */
    @PostMapping
    public ResponseEntity<SuccessResponseDto<UserResponseDto>> createUser(
            @Valid @RequestBody SignUpRequestDto dto,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                new SuccessResponseDto<>(
                        LocalDateTime.now(),
                        HttpStatus.CREATED.value(),
                        HttpStatus.CREATED.getReasonPhrase(),
                        httpRequest.getRequestURI(),
                        "유저를 성공적으로 삭제하였습니다.",
                        userService.createUser(dto)
                ),
                HttpStatus.CREATED);
    }
}
