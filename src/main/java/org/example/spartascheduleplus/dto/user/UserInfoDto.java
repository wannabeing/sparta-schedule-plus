package org.example.spartascheduleplus.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.example.spartascheduleplus.entity.user.User;

@Getter
@JsonIgnoreProperties // 존재하지 않는 필드가 들어오면 예외 발생 (default: false)
public class UserInfoDto {
    private final Long id;

    private final String name;

    private final String email;

    // ✅ 생성자 (User 객체를 받아 생성)
    public UserInfoDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
