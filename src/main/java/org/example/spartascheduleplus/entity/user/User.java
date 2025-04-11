package org.example.spartascheduleplus.entity.user;

import org.example.spartascheduleplus.entity.BaseEntity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
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

	// ✅ 생성자
	public User(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	/**
	 * 🚀 유저 정보 업데이트 메서드
	 * @param name 유저 이름
	 * @param email 유저 이메일
	 */
	public void updateUser(String name, @Nullable String email) {
		this.name = name;

		// email 값이 존재할 경우에만 수정
		if (email != null && !email.isBlank()) {
			this.email = email;
		}
	}

	/**
	 * 🚀 유저 비밀번호 업데이트 메서드
	 * @param password 새 비밀번호
	 */
	public void updatePassword(String password) {
		this.password = password;
	}

	/**
	 * 본인 id 인지 확인하는 메서드
	 * @param userId 유저 id
	 * @return 유저 여부를 반환 (true/false)
	 */
	public boolean isMyId(Long userId) {
		return this.getId().equals(userId);
	}
}
