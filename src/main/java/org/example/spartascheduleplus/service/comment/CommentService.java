package org.example.spartascheduleplus.service.comment;

import java.util.List;

import org.example.spartascheduleplus.dto.comment.CommentDetailResponseDto;
import org.example.spartascheduleplus.dto.comment.CommentResponseDto;
import org.example.spartascheduleplus.dto.comment.PagedCommentResponseDto;
import org.example.spartascheduleplus.dto.common.PageInfo;
import org.example.spartascheduleplus.dto.schedule.ScheduleInfoDto;
import org.example.spartascheduleplus.dto.user.UserInfoDto;
import org.example.spartascheduleplus.entity.comment.Comment;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.example.spartascheduleplus.entity.user.User;
import org.example.spartascheduleplus.exception.ResponseExceptionProvider;
import org.example.spartascheduleplus.repository.comment.CommentRepository;
import org.example.spartascheduleplus.service.schedule.ScheduleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final ScheduleService scheduleService;

	/**
	 * [Service] 일정의 모든 댓글을 조회하는 메서드
	 * @param schedule 일정 객체
	 * @param pageable 페이징 객체
	 * @return 페이징 댓글응답 객체 반환
	 */
	public PagedCommentResponseDto findAllComments(Schedule schedule, Pageable pageable) {
		// 일정, 유저 정보 DTO
		ScheduleInfoDto scheduleInfoDto = new ScheduleInfoDto(schedule);
		UserInfoDto userInfoDto = new UserInfoDto(schedule.getUser());

		Page<Comment> pagedComment = commentRepository.findAllByScheduleId(scheduleInfoDto.getId(), pageable);
		List<CommentResponseDto> comments = pagedComment
			.map(comment -> new CommentResponseDto(comment, userInfoDto))
			.getContent();

		return new PagedCommentResponseDto(
			new PageInfo(pagedComment),
			scheduleInfoDto,
			comments
		);
	}

	/**
	 * [Service] 댓글 조회하는 메서드
	 * @param commentId 댓글 id
	 * @return 댓글 객체
	 */
	public Comment findCommentById(Long commentId) {
		return commentRepository
			.findById(commentId)
			.orElseThrow(() -> ResponseExceptionProvider.notFound("존재하지 않는 댓글입니다."));
	}

	/**
	 * [Service] 댓글 생성하는 메서드
	 * @param comment 댓글 객체
	 * @param user 유저 객체
	 * @return 생성한 댓글응답 객체 반환
	 */
	public CommentDetailResponseDto createComment(Comment comment, User user) {
		return new CommentDetailResponseDto(
			commentRepository.save(comment),
			new UserInfoDto(user));
	}

	/**
	 * [Service] 댓글 삭제하는 메서드
	 * @param commentId 삭제하려는 댓글 id
	 */
	public void deleteComment(Long commentId) {
		commentRepository.deleteById(commentId);
	}
}
