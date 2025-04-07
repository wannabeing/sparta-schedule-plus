package org.example.spartascheduleplus.dto.schedule;

import java.time.LocalDateTime;

import org.example.spartascheduleplus.dto.comment.PagedCommentResponseDto;
import org.example.spartascheduleplus.entity.schedule.Schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;

@Getter
@JsonPropertyOrder({"id", "title", "contents", "createdAt", "updatedAt", "comments"})
public class ScheduleResponseDto {
	private final Long id;

	private final String title;

	private final String contents;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final PagedCommentResponseDto comments;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime updatedAt;

	// ✅ 생성자 (Schedule, 댓글리스트를 받아 생성)
	public ScheduleResponseDto(Schedule schedule, PagedCommentResponseDto comments) {
		this.id = schedule.getId();
		this.title = schedule.getTitle();
		this.contents = schedule.getContents();
		this.comments = comments;
		this.createdAt = schedule.getCreatedAt();
		this.updatedAt = schedule.getUpdatedAt();
	}
}
