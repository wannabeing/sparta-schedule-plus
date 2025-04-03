package org.example.spartascheduleplus.dto.user;

import lombok.Getter;
import org.example.spartascheduleplus.entity.user.User;

@Getter
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
