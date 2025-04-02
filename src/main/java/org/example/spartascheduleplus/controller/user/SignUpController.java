package org.example.spartascheduleplus.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.user.SignUpRequestDto;
import org.example.spartascheduleplus.dto.user.UserResponseDto;
import org.example.spartascheduleplus.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    /**
     * [Controller] 유저를 생성하는 메서드
     * @param dto 입력받은 유저요청 객체
     * @return 생성된 유저응답 객체 반환
     */
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody SignUpRequestDto dto
    ){
        return new ResponseEntity<>(userService.createUser(dto), HttpStatus.CREATED);
    }
}
