package org.example.spartascheduleplus.service.schedule;

import org.example.spartascheduleplus.dto.schedule.ScheduleDetailResponseDto;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.example.spartascheduleplus.exception.ResponseExceptionProvider;
import org.example.spartascheduleplus.repository.schedule.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final ScheduleRepository scheduleRepository;

	/**
	 * [Service] 전체 일정 조회 메서드
	 * @param pageable 페이징 객체
	 * @return 페이징 일정 객체 반환
	 */
	public Page<Schedule> findAllSchedules(Pageable pageable) {
		return scheduleRepository.findAll(pageable);
	}

	/**
	 * [Service] 단일 일정 조회하는 메서드
	 * @param id 사용자로부터 받은 일정 id
	 * @return 조회된 일정 객체 반환
	 */
	public Schedule findScheduleById(Long id) {
		return scheduleRepository
			.findById(id)
			.orElseThrow(() -> ResponseExceptionProvider.notFound("존재하지 않는 일정입니다."));
	}

	/**
	 * [Service] 일정 생성하는 메서드
	 * @param schedule 사용자가 입력한 일정요청 객체
	 * @return 생성한 일정응답 객체 반환
	 */
	public ScheduleDetailResponseDto createSchedule(Schedule schedule) {
		return new ScheduleDetailResponseDto(scheduleRepository.save(schedule), null);
	}

	/**
	 * [Service] 특정 일정 삭제 메서드
	 * @param scheduleId 삭제하려는 일정 id
	 */
	public void deleteSchedule(Long scheduleId) {
		scheduleRepository.deleteById(scheduleId);
	}

	/**
	 * [Service] 특정 일정 존재 여부 메서드
	 * @param scheduleId 일정 id
	 */
	public void existScheduleById(Long scheduleId) {
		if (!scheduleRepository.existsById(scheduleId)) {
			throw ResponseExceptionProvider.notFound("존재하지 않는 일정입니다.");
		}
	}
}
