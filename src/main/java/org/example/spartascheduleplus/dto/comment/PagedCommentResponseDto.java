package org.example.spartascheduleplus.dto.comment;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.example.spartascheduleplus.dto.common.PageInfo;
import org.example.spartascheduleplus.dto.schedule.ScheduleInfoDto;

import java.util.List;

@Getter
@JsonPropertyOrder({"page", "user", "schedule", "comments"})
public class PagedCommentResponseDto {
    private final PageInfo page;
    private final ScheduleInfoDto schedule;
    private final List<CommentResponseDto> comments;

    // ✅ 생성자 (페이징댓글, 유저, 일정 객체를 받아 생성)
    public PagedCommentResponseDto(
            PageInfo page,
            ScheduleInfoDto schedule,
            List<CommentResponseDto> comments)
    {
        this.page = page;
        this.schedule = schedule;
        this.comments = comments;
    }
}
