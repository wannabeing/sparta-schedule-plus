package org.example.spartascheduleplus.service.user;

import org.example.spartascheduleplus.dto.user.LoginRequestDto;
import org.example.spartascheduleplus.dto.user.SignUpRequestDto;
import org.example.spartascheduleplus.dto.user.UserPasswordRequestDto;
import org.example.spartascheduleplus.dto.user.UserRequestDto;
import org.example.spartascheduleplus.dto.user.UserResponseDto;
import org.example.spartascheduleplus.entity.user.User;
import org.example.spartascheduleplus.exception.ResponseExceptionProvider;
import org.example.spartascheduleplus.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * [Service] 유저를 생성하는 메서드
	 * @param dto 사용자가 입력한 가입요청 객체
	 * @return 생성된 유저응답 객체 반환
	 */
	public UserResponseDto createUser(SignUpRequestDto dto) {
		String encodedPassword = passwordEncoder.encode(dto.getPassword());

		User user = new User(dto.getName(), dto.getEmail(), encodedPassword);

		return new UserResponseDto(userRepository.save(user));
	}

	/**
	 * [Service] 로그인 하는 메서드
	 * @param dto 사용자가 입력한 로그인요청 객체
	 * @return 유저응답 객체 반환
	 */
	public UserResponseDto loginUser(LoginRequestDto dto) {
		User user = userRepository
			.findByEmail(dto.getEmail())
			.orElseThrow(() -> ResponseExceptionProvider.notFound("존재하지 않는 계정입니다."));

		// 비밀번호가 일치하지 않을 경우 예외 처리
		validatePasswordMatch(dto.getPassword(), user.getPassword());

		return new UserResponseDto(user);
	}

	/**
	 * [Service] 단일 유저를 조회하는 메서드
	 * @param id 유저 id
	 * @return 조회된 유저 객체
	 */
	public User findUser(Long id) {
		return userRepository
			.findById(id)
			.orElseThrow(() -> ResponseExceptionProvider.notFound("유효하지 않은 ID 입니다."));
	}

	/**
	 * [Service] 단일 유저를 수정하는 메서드
	 * @param dto 사용자가 입력한 유저요청 객체
	 * @param id 유저 id
	 * @return 수정된 유저응답 객체 반환
	 */
	@Transactional
	public UserResponseDto updateUser(UserRequestDto dto, Long id) {
		User existUser = this.findUser(id);

		// 이메일 입력을 안했을 경우, 이름만 업데이트
		if (dto.getEmail() == null || dto.getEmail().isBlank()) {
			existUser.updateUser(dto.getName(), null);
		}

		// 이메일 입력을 했을 경우
		else {
			// 이메일 중복체크
			validateEmailDuplicate(dto.getEmail(), existUser.getEmail());

			existUser.updateUser(dto.getName(), dto.getEmail());
		}

		return new UserResponseDto(existUser);
	}

	/**
	 * [Service] 유저의 비밀번호를 수정하는 메서드
	 * @param dto 비밀번호 요청 객체
	 * @param id 유저 id
	 */
	@Transactional
	public void updatePassword(UserPasswordRequestDto dto, Long id) {
		User existUser = this.findUser(id);

		// 현재 비밀번호가 일치하지 않을 경우 예외 처리
		validatePasswordMatch(dto.getCurrentPassword(), existUser.getPassword());

		String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
		existUser.updatePassword(encodedPassword);
	}

	/**
	 * [Service] 단일 유저를 삭제하는 메서드
	 * @param id 유저 id
	 */
	public void deleteUser(Long id) {
		this.findUser(id);

		userRepository.deleteById(id);
	}

	/**
	 * 🚀 입력한 비밀번호와 암호화 된 비밀번호가 일치하는지 확인하는 메서드
	 * @param rawPassword 입력한 비밀번호
	 * @param encodedPassword 암호화된 비밀번호
	 */
	private void validatePasswordMatch(String rawPassword, String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw ResponseExceptionProvider.unauthorized("비밀번호가 일치하지 않습니다.");
		}
	}

	/**
	 * 🚀 입력한 이메일의 중복 여부를 확인하는 메서드
	 * @param requestEmail 입력한 이메일
	 * @param userEmail 사용자의 이메일
	 */
	private void validateEmailDuplicate(String requestEmail, String userEmail) {
		boolean isDifferent = !userEmail.equals(requestEmail); // 사용자 기존 이메일과 입력 이메일이 다르면 true
		boolean isDuplicate = userRepository.existsByEmail(requestEmail); // 입력한 이메일이 이미 존재하면 true

		if (isDifferent && isDuplicate) {
			throw ResponseExceptionProvider.conflict("이미 사용 중인 이메일입니다.");
		}
	}
}
