package org.example.spartascheduleplus.repository.comment;

import org.example.spartascheduleplus.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * [Repo] 일정 id로 모든 댓글을 찾는 메서드
     * @param scheduleId 일정 id
     * @return 댓글객체 리스트를 반환
     */
    List<Comment> findAllByScheduleId(Long scheduleId);
}
