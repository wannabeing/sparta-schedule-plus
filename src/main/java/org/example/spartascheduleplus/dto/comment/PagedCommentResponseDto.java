package org.example.spartascheduleplus.dto.comment;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.example.spartascheduleplus.dto.common.PageInfo;
import org.example.spartascheduleplus.dto.schedule.ScheduleInfoDto;
import org.example.spartascheduleplus.dto.user.UserInfoDto;
import org.example.spartascheduleplus.entity.comment.Comment;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@JsonPropertyOrder({"page", "user", "schedule", "comments"})
public class PagedCommentResponseDto {
    private final PageInfo page;
    private final UserInfoDto user;
    private final ScheduleInfoDto schedule;
    private final List<CommentResponseDto> comments;

    // ✅ 생성자 (페이징댓글, 유저, 일정 객체를 받아 생성)
    public PagedCommentResponseDto(
            Page<Comment> pagedComment,
            UserInfoDto user,
            ScheduleInfoDto schedule)
    {
        this.page = new PageInfo(pagedComment);
        this.user = user;
        this.schedule = schedule;
        this.comments = pagedComment.map(CommentResponseDto::new).getContent();
    }
}
