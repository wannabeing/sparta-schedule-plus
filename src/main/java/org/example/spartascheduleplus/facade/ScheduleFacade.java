package org.example.spartascheduleplus.facade;

import java.util.List;

import org.example.spartascheduleplus.dto.comment.PagedCommentResponseDto;
import org.example.spartascheduleplus.dto.common.PageInfo;
import org.example.spartascheduleplus.dto.schedule.PagedScheduleResponseDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleDetailResponseDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleRequestDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleResponseDto;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.example.spartascheduleplus.entity.user.User;
import org.example.spartascheduleplus.exception.ResponseExceptionProvider;
import org.example.spartascheduleplus.service.comment.CommentService;
import org.example.spartascheduleplus.service.schedule.ScheduleService;
import org.example.spartascheduleplus.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduleFacade {
	private final UserService userService;
	private final ScheduleService scheduleService;
	private final CommentService commentService;

	/**
	 * [Facade] 일정 생성하는 메서드
	 * @param dto 사용자가 입력한 일정요청 객체
	 * @param loginUserId 로그인 유저 id
	 * @return 생성한 일정응답 객체 반환
	 */
	public ScheduleDetailResponseDto create(ScheduleRequestDto dto, Long loginUserId) {
		User loginUser = userService.findUser(loginUserId);
		Schedule schedule = new Schedule(dto, loginUser);

		return scheduleService.createSchedule(schedule);
	}

	/**
	 * [Facade] 전체 일정 조회 메서드
	 * @param pageable 페이징 객체
	 * @return 페이징일정 응답객체 반환
	 */
	public PagedScheduleResponseDto findAll(Pageable pageable) {
		Page<Schedule> pagedSchedule = scheduleService.findAllSchedules(pageable);

		List<ScheduleResponseDto> schedules = pagedSchedule
			.map(schedule -> {
				PagedCommentResponseDto comments = commentService.findAllComments(schedule, pageable);
				// DTO 로 반환
				return new ScheduleResponseDto(schedule, comments);
			}).getContent();

		return new PagedScheduleResponseDto(
			new PageInfo(pagedSchedule),
			schedules
		);
	}

	/**
	 * [Facade] 단일 일정 조회하는 메서드
	 * @param id 사용자로부터 받은 일정 id
	 * @return 상세 일정응답 객체를 반환
	 */
	public ScheduleDetailResponseDto findById(Long id) {
		Schedule schedule = scheduleService.findScheduleById(id);
		PagedCommentResponseDto comments = commentService.findAllComments(schedule, getDefaultPageable());

		return new ScheduleDetailResponseDto(scheduleService.findScheduleById(id), comments);
	}

	/**
	 * [Facade] 특정 일정 수정 메서드
	 * @param dto 사용자가 입력한 일정요청 객체
	 * @param scheduleId 일정 id
	 * @param loginUserId 로그인 유저 id
	 * @return 수정된 일정응답 객체
	 */
	@Transactional
	public ScheduleDetailResponseDto update(ScheduleRequestDto dto, Long scheduleId, Long loginUserId) {
		Schedule schedule = scheduleService.findScheduleById(scheduleId);

		// 사용자가 작성한 일정이 아닐 경우
		if (!loginUserId.equals(schedule.getUser().getId())) {
			throw ResponseExceptionProvider.forbidden("해당 일정의 수정권한이 없습니다.");
		}

		// 일정의 모든 댓글 가져오기 (기본 페이징)
		PagedCommentResponseDto comments = commentService.findAllComments(schedule, getDefaultPageable());

		schedule.updateSchedule(dto.getTitle(), dto.getContents());
		return new ScheduleDetailResponseDto(schedule, comments);
	}

	/**
	 * [Facade] 특정 일정 삭제 메서드
	 * @param scheduleId 삭제하려는 일정 id
	 * @param loginUserId 로그인유저 id
	 */
	public void delete(Long scheduleId, Long loginUserId) {
		Schedule schedule = scheduleService.findScheduleById(scheduleId);

		// 로그인유저가 작성한 일정인지 확인
		if (!schedule.getUser().getId().equals(loginUserId)) {
			throw ResponseExceptionProvider.forbidden("일정의 삭제권한이 없습니다.");
		}

		scheduleService.deleteSchedule(scheduleId);
	}

	/**
	 * 🚀 기본 페이징 객체를 반환하는 메서드 (생성일 내림차순)
	 * @return 페이징 객체를 반환
	 */
	private Pageable getDefaultPageable() {
		return PageRequest.of(
			0,
			10,
			Sort.by(Sort.Direction.DESC, "createdAt"));
	}
}
