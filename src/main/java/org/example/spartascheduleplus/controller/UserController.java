package org.example.spartascheduleplus.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.api.ApiResponseDto;
import org.example.spartascheduleplus.dto.user.*;
import org.example.spartascheduleplus.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * [Controller] 사용자 로그인 처리를 하는 메서드
     * @param dto 사용자가 입력한 로그인요청 객체
     * @param httpRequest Http 요청 객체
     * @return API 응답 객체 반환
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto> loginUser(
            @RequestBody LoginRequestDto dto,
            HttpServletRequest httpRequest)
    {
        UserResponseDto userResponseDto = userService.loginUser(dto);

        HttpSession session = httpRequest.getSession(); // ✅ 기존 세션 또는 새로운 세션을 생성
        session.setAttribute("loginUser", userResponseDto);

        return new ResponseEntity<>(
                new ApiResponseDto("success", "로그인에 성공했습니다."),
                HttpStatus.OK
        );
    }

    /**
     * [Controller] 로그인 사용자 정보 반환 메서드
     * @param loginUser 세션에 저장된 유저 정보 객체
     * @return 로그인한 유저응답 객체 반환
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyInfo(
            @SessionAttribute(
                    name = "loginUser",
                    required = false)
            UserResponseDto loginUser
    ){
        return new ResponseEntity<>(
                new UserResponseDto(userService.findUser(loginUser.getId())),
                HttpStatus.OK);
    }

    /**
     * [Controller] 유저를 찾아 정보를 반환하는 메서드
     * @param id 유저 id
     * @return 찾은 유저응답 객체 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findUser(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(
                new UserResponseDto(userService.findUser(id)),
                HttpStatus.OK);
    }


    /**
     * [Controller] 유저 정보를 업데이트하고 반환하는 메서드
     * @param dto 사용자가 입력한 수정 요청 객체
     * @param loginUser 세션에 저장된 유저 정보 객체
     * @return 수정된 유저응답 객체 반환
     */
    @PostMapping("/update")
    public ResponseEntity<UserResponseDto> updateUser(
            @Valid @RequestBody UserRequestDto dto,
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser
    ){
        return new ResponseEntity<>(
                userService.updateUser(dto, loginUser.getId()),
                HttpStatus.OK);
    }

    /**
     * [Controller] 유저 비밀번호를 업데이트하고 성공여부 반환하는 메서드
     * @param dto 사용자가 입력한 요청객체
     * @param loginUser 세션에 저장된 유저 정보 객체
     * @return API 응답객체 반환
     */
    @PostMapping("/password")
    public ResponseEntity<ApiResponseDto> updatePassword(
            @Valid @RequestBody UserPasswordRequestDto dto,
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser
    ){
        return new ResponseEntity<>(
                userService.updatePassword(dto, loginUser.getId()),
                HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDto> logoutUser(
            HttpServletRequest httpRequest)
    {
        HttpSession session = httpRequest.getSession(false); // ✅ 기존 세션
        session.removeAttribute("loginUser"); // ✅ 세션에 다른 정보가 있다고 가정 (ex. 장바구니, 마지막페이지 등)

        return new ResponseEntity<>(
                new ApiResponseDto("success", "로그아웃하셨습니다."),
                HttpStatus.OK
        );
    }

    /**
     * [Controller] 유저를 삭제하고 성공여부를 반환하는 메서드
     * @param loginUser 세션에 저장된 유저 정보 객체
     * @return API 응답 객체 반환
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponseDto> deleteUser(
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser
    ){
        return new ResponseEntity<>(
                userService.deleteUser(loginUser.getId()),
                HttpStatus.OK);
    }
}
