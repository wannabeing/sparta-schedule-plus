package org.example.spartascheduleplus.facade;

import org.example.spartascheduleplus.dto.comment.CommentDetailResponseDto;
import org.example.spartascheduleplus.dto.comment.CommentRequestDto;
import org.example.spartascheduleplus.dto.comment.PagedCommentResponseDto;
import org.example.spartascheduleplus.dto.user.UserInfoDto;
import org.example.spartascheduleplus.entity.comment.Comment;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.example.spartascheduleplus.entity.user.User;
import org.example.spartascheduleplus.exception.ResponseExceptionProvider;
import org.example.spartascheduleplus.service.comment.CommentService;
import org.example.spartascheduleplus.service.schedule.ScheduleService;
import org.example.spartascheduleplus.service.user.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentFacade {

	private final ScheduleService scheduleService;
	private final UserService userService;
	private final CommentService commentService;

	/**
	 * [Facade] 댓글 생성하는 메서드
	 * @param dto 사용자가 입력한 댓글요청 객체
	 * @param scheduleId 일정 id
	 * @param userId 유저 id
	 * @return 생성한 댓글응답 객체 반환
	 */
	public CommentDetailResponseDto create(CommentRequestDto dto, Long scheduleId, Long userId) {
		Schedule schedule = scheduleService.findScheduleById(scheduleId);
		User user = userService.findUser(userId);
		Comment comment = new Comment(dto.getComment(), schedule, user);

		return commentService.createComment(comment, user);
	}

	/**
	 * [Facade] 일정의 모든 댓글을 조회하는 메서드
	 * @param scheduleId 일정 id
	 * @param pageable 페이징 객체
	 * @return 페이징 댓글응답 객체 반환
	 */
	public PagedCommentResponseDto findAll(Long scheduleId, Pageable pageable) {
		// 존재하는 일정인지 체크
		Schedule schedule = scheduleService.findScheduleById(scheduleId);

		return commentService.findAllComments(schedule, pageable);
	}

	/**
	 * [Facade] 댓글 조회하는 메서드
	 * @param commentId 댓글 id
	 * @return 댓글응답 객체를 반환
	 */
	public CommentDetailResponseDto findById(Long commentId, Long scheduleId) {
		UserInfoDto userInfoDto = new UserInfoDto(scheduleService.findScheduleById(scheduleId).getUser());
		Comment comment = commentService.findCommentById(commentId);

		return new CommentDetailResponseDto(comment, userInfoDto);
	}

	/**
	 * [Facade] 댓글 수정하는 메서드
	 * @param dto 사용자가 입력한 댓글요청 객체
	 * @param commentId 댓글 id
	 * @param loginUserId 로그인 유저 id
	 * @return 댓글 응답객체 반환
	 */
	@Transactional
	public CommentDetailResponseDto update(
		CommentRequestDto dto, Long commentId, Long loginUserId) {
		Comment existComment = commentService.findCommentById(commentId);
		User commentUser = existComment.getUser();

		// 사용자가 작성한 일정이 아닐 경우
		if (commentUser.isMyId(loginUserId)) {
			throw ResponseExceptionProvider.forbidden("해당 댓글의 수정권한이 없습니다.");
		}

		existComment.updateComment(dto.getComment());
		return new CommentDetailResponseDto(
			existComment,
			new UserInfoDto(commentUser));
	}

	/**
	 * [Facade] 댓글 삭제하는 메서드
	 * @param commentId 삭제하려는 댓글 id
	 * @param scheduleId 일정 id
	 * @param loginUserId 로그인유저 id
	 */
	public void delete(
		Long commentId, Long scheduleId, Long loginUserId) {
		// 존재하는 일정인지 체크
		scheduleService.findScheduleById(scheduleId);
		Comment comment = commentService.findCommentById(commentId);

		// 로그인유저가 작성한 댓글인지 확인
		if (comment.isMyComment(loginUserId)) {
			throw ResponseExceptionProvider.unauthorized("댓글의 삭제권한이 없습니다.");
		}

		commentService.deleteComment(commentId);
	}
}
