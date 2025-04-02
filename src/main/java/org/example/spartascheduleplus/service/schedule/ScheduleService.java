package org.example.spartascheduleplus.service.schedule;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.schedule.ScheduleRequestDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleResponseDto;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.example.spartascheduleplus.entity.user.User;
import org.example.spartascheduleplus.repository.schedule.ScheduleRepository;
import org.example.spartascheduleplus.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public ScheduleResponseDto createSchedule(
            ScheduleRequestDto dto,
            Long loginUserId){
        User loginUser = userService.findUser(loginUserId);

        Schedule schedule = new Schedule(dto);
        schedule.setUser(loginUser);

        return new ScheduleResponseDto(scheduleRepository.save(schedule));
    }

    /**
     * [Service] 단일 일정 조회하는 메서드
     * @param id 사용자로부터 받은 일정 id
     * @return 조회된 일정 객체 반환
     */
    public Schedule findScheduleById(Long id) {
        return scheduleRepository
                .findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 ID 입니다."));
    }

    /**
     * 전체 일정 조회 메서드
     * @return 전체일정 목록 반환
     */
    public List<ScheduleResponseDto> findAllSchedules() {
        List<Schedule> AllSchedules = scheduleRepository.findAll();

        return AllSchedules
                .stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }

    /**
     * [Service 특정 일정 수정 메서드
     * @param dto 사용자가 입력한 일정요청 객체
     * @param scheduleId 일정 id
     * @param loginUserId 로그인 유저 id
     * @return 수정된 일정응답 객체
     */
    @Transactional
    public ScheduleResponseDto updateSchedule(ScheduleRequestDto dto, Long scheduleId, Long loginUserId) {

        Schedule existSchedule = this.findScheduleById(scheduleId);

        // 사용자가 작성한 일정이 아닐 경우
        if(!loginUserId.equals(existSchedule.getUser().getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "해당 일정의 수정권한이 없습니다.");
        }

        existSchedule.updateSchedule(dto.getTitle(), dto.getContents());

        return new ScheduleResponseDto(existSchedule);
    }

    /**
     * [Service] 특정 일정 삭제 메서드
     * @param id 사용자로부터 받은 일정 id
     */
    public void deleteSchedule(Long id) {
        // 존재하는 일정인지 체크
        this.findScheduleById(id);

        scheduleRepository.deleteById(id);
    }
}
