package org.example.spartascheduleplus.controller.user;

import java.time.LocalDateTime;

import org.example.spartascheduleplus.dto.api.SuccessResponseDto;
import org.example.spartascheduleplus.dto.user.LoginRequestDto;
import org.example.spartascheduleplus.dto.user.UserPasswordRequestDto;
import org.example.spartascheduleplus.dto.user.UserRequestDto;
import org.example.spartascheduleplus.dto.user.UserResponseDto;
import org.example.spartascheduleplus.exception.ResponseExceptionProvider;
import org.example.spartascheduleplus.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	/**
	 * [Controller] 사용자 로그인 처리를 하는 메서드
	 * @param dto 사용자가 입력한 로그인요청 객체
	 * @param httpRequest Http 요청 객체
	 * @return API 성공 응답객체 반환
	 */
	@PostMapping("/login")
	public ResponseEntity<SuccessResponseDto> loginUser(
		@RequestBody LoginRequestDto dto,
		HttpServletRequest httpRequest) {
		// 이미 로그인 되었을 경우에 예외 처리
		HttpSession session = httpRequest.getSession(false);
		if (session != null && session.getAttribute("loginUser") != null) {
			throw ResponseExceptionProvider.conflict("이미 로그인하셨습니다.");
		}

		// 로그인 및 유저 응답객체 반환
		UserResponseDto userResponseDto = userService.loginUser(dto);

		// 세션에 로그인 정보 저장
		httpRequest.getSession().setAttribute("loginUser", userResponseDto);

		return new ResponseEntity<>(
			new SuccessResponseDto<>(
				LocalDateTime.now(),
				HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(),
				httpRequest.getRequestURI(),
				"로그인에 성공했습니다.",
				userResponseDto
			),
			HttpStatus.OK
		);
	}

	/**
	 * [Controller] 로그인 사용자 정보 반환 메서드
	 * @param loginUser 세션에 저장된 유저 정보 객체
	 * @param httpRequest Http 요청 객체
	 * @return API 성공 응답객체 반환
	 */
	@GetMapping("/me")
	public ResponseEntity<SuccessResponseDto<UserResponseDto>> getMyInfo(
		@SessionAttribute(
			name = "loginUser",
			required = false)
		UserResponseDto loginUser,
		HttpServletRequest httpRequest) {
		return new ResponseEntity<>(
			new SuccessResponseDto<>(
				LocalDateTime.now(),
				HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(),
				httpRequest.getRequestURI(),
				"로그인 유저 정보입니다.",
				new UserResponseDto(userService.findUser(loginUser.getId()))
			),
			HttpStatus.OK
		);
	}

	/**
	 * [Controller] 유저를 찾아 정보를 반환하는 메서드
	 * @param id 유저 id
	 * @param httpRequest Http 요청 객체
	 * @return API 성공 응답객체 반환
	 */
	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponseDto<UserResponseDto>> findUser(
		@PathVariable Long id,
		HttpServletRequest httpRequest) {
		return new ResponseEntity<>(
			new SuccessResponseDto<>(
				LocalDateTime.now(),
				HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(),
				httpRequest.getRequestURI(),
				"조회된 유저 정보입니다.",
				new UserResponseDto(userService.findUser(id))
			),
			HttpStatus.OK);
	}

	/**
	 * [Controller] 유저 정보를 업데이트하고 반환하는 메서드
	 * @param dto 사용자가 입력한 수정 요청 객체
	 * @param httpRequest Http 요청 객체
	 * @return API 성공 응답객체 반환
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<SuccessResponseDto<UserResponseDto>> updateUser(
		@Valid @RequestBody UserRequestDto dto,
		@SessionAttribute(
			name = "loginUser")
		UserResponseDto loginUser,
		HttpServletRequest httpRequest) {
		return new ResponseEntity<>(
			new SuccessResponseDto<>(
				LocalDateTime.now(),
				HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(),
				httpRequest.getRequestURI(),
				"유저 업데이트가 완료되었습니다.",
				userService.updateUser(dto, loginUser.getId())
			),
			HttpStatus.OK);
	}

	/**
	 * [Controller] 유저 비밀번호를 업데이트하고 성공여부 반환하는 메서드
	 * @param dto 사용자가 입력한 요청객체
	 * @param loginUser 세션에 저장된 유저 정보 객체
	 * @param httpRequest Http 요청 객체
	 * @return API 성공 응답객체 반환
	 */
	@PatchMapping("/password")
	public ResponseEntity<SuccessResponseDto> updatePassword(
		@Valid @RequestBody UserPasswordRequestDto dto,
		@SessionAttribute(
			name = "loginUser")
		UserResponseDto loginUser,
		HttpServletRequest httpRequest) {
		// 비밀번호 업데이트
		userService.updatePassword(dto, loginUser.getId());

		return new ResponseEntity<>(
			new SuccessResponseDto<>(
				LocalDateTime.now(),
				HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(),
				httpRequest.getRequestURI(),
				"비밀번호를 업데이트 하였습니다.",
				null
			),
			HttpStatus.OK);

	}

	/**
	 * [Controller] 로그아웃 기능의 메서드
	 * @param httpRequest Http 요청 객체
	 * @return API 성공 응답객체 반환
	 */
	@PostMapping("/logout")
	public ResponseEntity<SuccessResponseDto> logoutUser(
		HttpServletRequest httpRequest) {
		// 이미 로그아웃 되었을 경우에 예외 처리
		HttpSession session = httpRequest.getSession(false);
		if (session == null || session.getAttribute("loginUser") == null) {
			throw ResponseExceptionProvider.conflict("이미 로그아웃 하셨습니다.");
		}

		// 세션에서 로그인 정보 삭제
		session.removeAttribute("loginUser"); // ✅ 세션에 다른 정보가 있다고 가정 (ex. 장바구니, 마지막페이지 등)

		return new ResponseEntity<>(
			new SuccessResponseDto<>(
				LocalDateTime.now(),
				HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(),
				httpRequest.getRequestURI(),
				"로그아웃 하였습니다.",
				null
			),
			HttpStatus.OK);
	}

	/**
	 * [Controller] 유저를 삭제하고 성공여부를 반환하는 메서드
	 * @param loginUser 세션에 저장된 유저 정보 객체
	 * @param httpRequest Http 요청 객체
	 * @return API 성공 응답객체 반환
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponseDto> deleteUser(
		@SessionAttribute(
			name = "loginUser")
		UserResponseDto loginUser,
		HttpServletRequest httpRequest) {
		// 유저 삭제
		userService.deleteUser(loginUser.getId());

		return new ResponseEntity<>(
			new SuccessResponseDto<>(
				LocalDateTime.now(),
				HttpStatus.OK.value(),
				HttpStatus.OK.getReasonPhrase(),
				httpRequest.getRequestURI(),
				"유저를 성공적으로 삭제하였습니다.",
				null
			),
			HttpStatus.OK);
	}
}
