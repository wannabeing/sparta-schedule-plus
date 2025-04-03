package org.example.spartascheduleplus.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.example.spartascheduleplus.dto.schedule.ScheduleInfoDto;
import org.example.spartascheduleplus.dto.user.UserInfoDto;
import org.example.spartascheduleplus.entity.comment.Comment;

import java.time.LocalDateTime;


@Getter
public class CommentResponseDto {
    private final Long commentId;

    private final String comment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    private final UserInfoDto user;

    private final ScheduleInfoDto schedule;

    // ✅ 생성자 (Comment 객체를 받아 생성)
    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.user = new UserInfoDto(comment.getUser());
        this.schedule = new ScheduleInfoDto(comment.getSchedule());
    }
}
