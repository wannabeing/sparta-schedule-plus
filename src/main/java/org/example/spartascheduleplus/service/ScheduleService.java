package org.example.spartascheduleplus.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.ApiResponseDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleRequestDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleResponseDto;
import org.example.spartascheduleplus.entity.Schedule;
import org.example.spartascheduleplus.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository repository;

    /**
     * [Service] 일정 생성하는 메서드
     * @param dto 사용자가 입력한 일정요청 객체
     * @return 생성한 일정응답 객체 반환
     */
    public ScheduleResponseDto createSchedule(ScheduleRequestDto dto){
        Schedule schedule = new Schedule(dto);
        return new ScheduleResponseDto(repository.save(schedule));
    }

    /**
     * [Service] 단일 일정 조회하는 메서드
     * @param id 사용자로부터 받은 일정 id
     * @return 조회된 일정 객체 반환
     */
    public Schedule findScheduleById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 ID 입니다."));
    }

    /**
     * 전체 일정 조회 메서드
     * @return 전체일정 목록 반환
     */
    public List<ScheduleResponseDto> findAllSchedules() {
        List<Schedule> AllSchedules = repository.findAll();

        return AllSchedules
                .stream()
                .map(ScheduleResponseDto::new)
                .toList();
    }

    /**
     * [Service 특정 일정 수정 메서드
     * @param dto 사용자가 입력한 일정요청 객체
     * @param id 사용자로부터 받은 일정 id
     * @return 수정된 일정응답 객체
     */
    @Transactional
    public ScheduleResponseDto updateSchedule(ScheduleRequestDto dto, Long id) {
        Schedule existSchedule = this.findScheduleById(id);

        existSchedule.updateSchedule(dto.getTitle(), dto.getContents());

        return new ScheduleResponseDto(existSchedule);
    }

    /**
     * [Service] 특정 일정 삭제 메서드
     * @param id 사용자로부터 받은 일정 id
     * @return API 응답 객체 반환
     */
    public ApiResponseDto deleteSchedule(Long id) {
        repository.existsByIdOrElseThrow(id);
        repository.deleteById(id);

        return new ApiResponseDto("success", "성공적으로 삭제하였습니다.");
    }
}
