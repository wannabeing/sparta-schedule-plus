package org.example.spartascheduleplus.service.schedule;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.schedule.PagedScheduleResponseDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleDetailResponseDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleRequestDto;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.example.spartascheduleplus.entity.user.User;
import org.example.spartascheduleplus.exception.ResponseExceptionProvider;
import org.example.spartascheduleplus.repository.schedule.ScheduleRepository;
import org.example.spartascheduleplus.service.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final UserService userService;

    /**
     * [Service] 일정 생성하는 메서드
     * @param dto 사용자가 입력한 일정요청 객체
     * @param loginUserId 세션에 저장된 유저 id
     * @return 생성한 일정응답 객체 반환
     */
    public ScheduleDetailResponseDto createSchedule(
            ScheduleRequestDto dto,
            Long loginUserId){
        User loginUser = userService.findUser(loginUserId);

        Schedule schedule = new Schedule(dto, loginUser);

        return new ScheduleDetailResponseDto(scheduleRepository.save(schedule));
    }

    /**
     * [Service] 단일 일정 조회하는 메서드
     * @param id 사용자로부터 받은 일정 id
     * @return 조회된 일정 객체 반환
     */
    public Schedule findScheduleById(Long id)
    {
        return scheduleRepository
                .findById(id)
                .orElseThrow(()-> ResponseExceptionProvider.notFound("존재하지 않는 일정입니다."));
    }

    /**
     * [Service] 상세 일정응답 객체를 생성하는 메서드
     * @param id 일정 id
     * @return 상세 일정응답 객체를 반환
     */
    public ScheduleDetailResponseDto createDetailScheduleDto(Long id)
    {
        return new ScheduleDetailResponseDto(findScheduleById(id));
    }

    /**
     * 전체 일정 조회 메서드
     * @param pageable 페이징 객체
     * @return 페이징일정 응답객체 반환
     */
    public PagedScheduleResponseDto findAllSchedules(Pageable pageable)
    {
        Page<Schedule> pagedSchedule = scheduleRepository.findAll(pageable);

        return new PagedScheduleResponseDto(pagedSchedule);
    }

    /**
     * [Service 특정 일정 수정 메서드
     * @param dto 사용자가 입력한 일정요청 객체
     * @param scheduleId 일정 id
     * @param loginUserId 로그인 유저 id
     * @return 수정된 일정응답 객체
     */
    @Transactional
    public ScheduleDetailResponseDto updateSchedule(
            ScheduleRequestDto dto, Long scheduleId, Long loginUserId)
    {
        Schedule existSchedule = this.findScheduleById(scheduleId);

        // 사용자가 작성한 일정이 아닐 경우
        if(!loginUserId.equals(existSchedule.getUser().getId())){
            throw ResponseExceptionProvider.forbidden("해당 일정의 수정권한이 없습니다.");
        }

        existSchedule.updateSchedule(dto.getTitle(), dto.getContents());
        return new ScheduleDetailResponseDto(existSchedule);
    }

    /**
     * [Service] 특정 일정 삭제 메서드
     * @param scheduleId 삭제하려는 일정 id
     * @param loginUserId 로그인유저 id
     */
    public void deleteSchedule(Long scheduleId, Long loginUserId)
    {
        Schedule existSchedule = this.findScheduleById(scheduleId);

        // 로그인유저가 작성한 일정인지 확인
        if(!existSchedule.getUser().getId().equals(loginUserId)){
            throw ResponseExceptionProvider.forbidden("일정의 삭제권한이 없습니다.");
        }
        scheduleRepository.deleteById(scheduleId);
    }
}
