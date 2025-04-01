package org.example.spartascheduleplus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.ApiResponseDto;
import org.example.spartascheduleplus.dto.user.UserRepsonseDto;
import org.example.spartascheduleplus.dto.user.UserRequestDto;
import org.example.spartascheduleplus.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserRepsonseDto> createUser(
            @Valid @RequestBody UserRequestDto dto
    ){
        return new ResponseEntity<>(userService.createUser(dto), HttpStatus.OK);
    }

    // FIXME: 로그인된 유저 정보 반환으로 수정해야 함
    @GetMapping("/{id}")
    public ResponseEntity<UserRepsonseDto> findUser(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(new UserRepsonseDto(userService.findUser(id)), HttpStatus.OK);
    }

    // FIXME: 로그인된 유저 정보로 수정해야 함
    @PostMapping("/{id}")
    public ResponseEntity<UserRepsonseDto> updateUser(
            @Valid @RequestBody UserRequestDto dto,
            @PathVariable Long id
    ){
        return new ResponseEntity<>(userService.updateUser(dto, id), HttpStatus.OK);
    }

    // FIXME: 로그인된 유저 정보로 수정해야 함
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteUser(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(userService.deleteUser(id), HttpStatus.OK);
    }
}
