package org.example.spartascheduleplus.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.ApiResponseDto;
import org.example.spartascheduleplus.dto.user.UserRepsonseDto;
import org.example.spartascheduleplus.dto.user.UserRequestDto;
import org.example.spartascheduleplus.entity.User;
import org.example.spartascheduleplus.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public UserRepsonseDto createUser(UserRequestDto dto) {
        User user = new User(dto);
        return new UserRepsonseDto(repository.save(user));
    }

    public User findUser(Long id) {
        return repository
                .findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 ID 입니다."));
    }

    @Transactional
    public UserRepsonseDto updateUser(UserRequestDto dto, Long id) {
        User existUser = this.findUser(id);
        existUser.updateUser(dto.getName(), dto.getEmail());

        return new UserRepsonseDto(existUser);
    }

    public ApiResponseDto deleteUser(Long id) {
        repository.existsByIdOrElseThrow(id);
        repository.deleteById(id);

        return new ApiResponseDto("success", "성공적으로 삭제하였습니다.");
    }
}
