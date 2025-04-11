package org.example.spartascheduleplus.entity.comment;

import org.example.spartascheduleplus.entity.BaseEntity;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.example.spartascheduleplus.entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "text")
	private String comment;

	@ManyToOne
	@JoinColumn(name = "schedule_id")
	@Setter
	private Schedule schedule;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@Setter
	private User user;

	// ✅ 생성자
	public Comment(String comment, Schedule schedule, User user) {
		this.comment = comment;
		this.schedule = schedule;
		this.user = user;
	}

	/**
	 * 🚀 댓글 업데이트 메서드
	 * @param comment 댓글 객체
	 */
	public void updateComment(String comment) {
		this.comment = comment;
	}

	/**
	 * 🚀 id 값으로 댓글 작성자 본인인지 확인하는 메서드
	 * @param id 유저 id
	 * @return 본인인지 확인하는 값을 반환 (true/false)
	 */
	public boolean isMyComment(Long id) {
		return this.user.getId() != null && this.user.getId().equals(id);
	}
}
