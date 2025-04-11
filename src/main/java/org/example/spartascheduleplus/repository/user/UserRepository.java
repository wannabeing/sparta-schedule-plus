package org.example.spartascheduleplus.repository.user;

import java.util.Optional;

import org.example.spartascheduleplus.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * [Repo] 이메일로 사용자가 존재하는지 확인하는 메서드
	 * @param email 사용자 email
	 * return Optional User 객체 반환
	 */
	Optional<User> findByEmail(String email);

	/**
	 * [Repo] 이메일로 사용자 존재하는지 체크하는 메서드
	 * @param email 사용자 email
	 * @return 존재하면 true 반환
	 */
	boolean existsByEmail(String email);
}
