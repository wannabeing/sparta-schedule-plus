package org.example.spartascheduleplus.repository.comment;

import org.example.spartascheduleplus.entity.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * [Repo] 일정 id로 모든 댓글을 찾는 메서드
     * @param scheduleId 일정 id
     * @param pageable 페이징 객체
     * @return 페이징 댓글 객체를 반환
     */
    Page<Comment> findAllByScheduleId(Long scheduleId, Pageable pageable);
}
