package org.example.spartascheduleplus.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.api.ApiResponseDto;
import org.example.spartascheduleplus.dto.user.*;
import org.example.spartascheduleplus.entity.user.User;
import org.example.spartascheduleplus.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    /**
     * [Service] 유저를 생성하는 메서드
     * @param dto 사용자가 입력한 가입요청 객체
     * @return 생성된 유저응답 객체 반환
     */
    public UserResponseDto createUser(SignUpRequestDto dto) {
        User user = new User(dto);
        return new UserResponseDto(repository.save(user));
    }

    /**
     * [Service] 로그인 하는 메서드
     * @param dto 사용자가 입력한 로그인요청 객체
     * @return 유저응답 객체 반환
     */
    public UserResponseDto loginUser(LoginRequestDto dto){
        User user = repository
                .findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 계정 입니다."));

        // [예외] 비밀번호가 일치하지 않을 경우 예외 처리
        if (!dto.getPassword().equals(user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        return new UserResponseDto(user);
    }

    /**
     * [Service] 단일 유저를 조회하는 메서드
     * @param id 유저 id
     * @return 조회된 유저 객체
     */
    public User findUser(Long id) {
        return repository
                .findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 ID 입니다."));
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
        existUser.updateUser(dto.getName(), dto.getEmail());

        return new UserResponseDto(existUser);
    }

    /**
     * [Service] 유저의 비밀번호를 수정하는 메서드
     * @param dto 비밀번호 요청 객체
     * @param id 유저 id
     * @return API 응답 객체 반환
     */
    @Transactional
    public ApiResponseDto updatePassword(UserPasswordRequestDto dto, Long id){

        User existUser = this.findUser(id);

        // [예외] 비밀번호가 일치하지 않을 경우 예외 처리
        if (!dto.getCurrentPassword().equals(existUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        existUser.updatePassword(dto.getNewPassword());

        return new ApiResponseDto("success", "비밀번호가 변경되었습니다.");
    }

    /**
     * [Service] 단일 유저를 삭제하는 메서드
     * @param id 유저 id
     * @return API 응답 객체 반환
     */
    public ApiResponseDto deleteUser(Long id) {
        this.findUser(id);
        repository.deleteById(id);

        return new ApiResponseDto("success", "성공적으로 삭제하였습니다.");
    }
}
