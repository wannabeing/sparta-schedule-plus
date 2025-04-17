package org.example.spartascheduleplus.service.user;

import static org.mockito.BDDMockito.*;

import org.assertj.core.api.Assertions;
import org.example.spartascheduleplus.dto.user.SignUpRequestDto;
import org.example.spartascheduleplus.dto.user.UserResponseDto;
import org.example.spartascheduleplus.entity.user.User;
import org.example.spartascheduleplus.repository.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@Mock
	private UserRepository userRepository;

	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	UserService userService;

	@Test
	void 유저_생성_부터_저장_까지() {
		// ✅ given (상황이 주어지고)
		SignUpRequestDto requestDto =
			new SignUpRequestDto("choihyuk", "wannabeing@naver.com", "1234qwer");

		// 비밀번호 인코딩 설정
		given(passwordEncoder.encode("1234qwer")).willReturn("encode");

		// save 메서드 실행되면, 감지하여 0번째 User 객체를 캡쳐하여 반환
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		given(userRepository.save(userCaptor.capture()))
			.willAnswer(invocation ->
				invocation.getArgument(0));

		// ✅ when (실행해 보고)
		UserResponseDto responseDto = userService.createUser(requestDto);

		// ✅ then (원하는 결과)
		User user = userCaptor.getValue();
		Assertions.assertThat(user.getPassword()).isEqualTo("encode");
		Assertions.assertThat(user.getEmail()).isEqualTo("wannabeing@naver.com");
		Assertions.assertThat(user.getName()).isEqualTo("choihyuk");
	}

	@Test
	void loginUser() {
	}

	@Test
	void findUser() {
	}

	@Test
	void updateUser() {
	}

	@Test
	void updatePassword() {
	}

	@Test
	void deleteUser() {
	}
}