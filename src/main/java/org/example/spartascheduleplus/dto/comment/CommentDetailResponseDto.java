package org.example.spartascheduleplus.dto.comment;

import lombok.Getter;
import org.example.spartascheduleplus.dto.schedule.ScheduleInfoDto;
import org.example.spartascheduleplus.dto.user.UserInfoDto;
import org.example.spartascheduleplus.entity.comment.Comment;



@Getter
public class CommentDetailResponseDto extends CommentResponseDto {
    private final ScheduleInfoDto schedule;

    // ✅ 생성자 (Comment 객체를 받아 생성)
    public CommentDetailResponseDto(Comment comment, UserInfoDto user) {
        super(comment, user); // 부모 생성자 호출
        this.schedule = new ScheduleInfoDto(comment.getSchedule());
    }
}
