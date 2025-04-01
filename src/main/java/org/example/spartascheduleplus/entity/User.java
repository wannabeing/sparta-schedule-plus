package org.example.spartascheduleplus.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.example.spartascheduleplus.dto.user.UserRepsonseDto;
import org.example.spartascheduleplus.dto.user.UserRequestDto;

@Entity
@Table(name="user")
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    // ✅ 기본 생성자
    public User(){}

    // ✅ 생성자 (RequestDTO 객체를 받아 생성)
    public User(UserRequestDto dto){
        this.name = dto.getName();
        this.email = dto.getEmail();
    }

    // ✅ 생성자 (ResponseDTO 객체를 받아 생성)
    public User(UserRepsonseDto dto){
        this.name = dto.getName();
        this.email = dto.getEmail();
    }

    /**
     * 🚀 유저 업데이트 메서드
     * @param name 유저 이름
     * @param email 유저 이메일
     */
    public void updateUser(String name, String email){
        this.name = name;
        this.email = email;
    }
}
