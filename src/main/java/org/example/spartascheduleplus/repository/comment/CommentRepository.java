package org.example.spartascheduleplus.repository.comment;

import org.example.spartascheduleplus.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByScheduleId(Long scheduleId);
}
