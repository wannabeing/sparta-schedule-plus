package org.example.spartascheduleplus.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.spartascheduleplus.dto.user.SignUpRequestDto;
import org.example.spartascheduleplus.entity.BaseEntity;

@Entity
@Table(name="user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // ✅ 생성자 (SignUpRequestDTO 객체를 받아 생성)
    public User(SignUpRequestDto dto){
        this.name = dto.getName();
        this.email = dto.getEmail();
        this.password = dto.getPassword();
    }

    /**
     * 🚀 유저 정보 업데이트 메서드
     * @param name 유저 이름
     * @param email 유저 이메일
     */
    public void updateUser(String name, String email){
        this.name = name;
        this.email = email;
    }

    /**
     * 🚀 유저 비밀번호 업데이트 메서드
     * @param password 새 비밀번호
     */
    public void updatePassword(String password) {
        this.password = password;
    }
}
