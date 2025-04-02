package org.example.spartascheduleplus.dto.user;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.example.spartascheduleplus.entity.User;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private final Long id;

    private final String name;

    private final String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    // ✅ 생성자 (User 객체를 받아 생성)
    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
