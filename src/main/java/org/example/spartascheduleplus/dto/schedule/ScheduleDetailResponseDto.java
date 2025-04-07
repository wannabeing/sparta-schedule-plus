package org.example.spartascheduleplus.dto.schedule;

import lombok.Getter;
import org.example.spartascheduleplus.dto.comment.PagedCommentResponseDto;
import org.example.spartascheduleplus.dto.user.UserInfoDto;
import org.example.spartascheduleplus.entity.schedule.Schedule;


@Getter
public class ScheduleDetailResponseDto extends ScheduleResponseDto {
    private final UserInfoDto user;

    // ✅ 생성자 (Schedule, 댓글리스트를 받아 생성)
    public ScheduleDetailResponseDto(Schedule schedule, PagedCommentResponseDto comments)
    {
        super(schedule, comments); // 부모 생성자 호출
        this.user = new UserInfoDto(schedule.getUser());
    }
}
