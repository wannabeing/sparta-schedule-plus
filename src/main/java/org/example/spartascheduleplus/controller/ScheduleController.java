package org.example.spartascheduleplus.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.spartascheduleplus.dto.ApiResponseDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleRequestDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleResponseDto;
import org.example.spartascheduleplus.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@AllArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * [Controller] 일정 생성 메서드
     * @param dto 사용자가 입력한 일정요청 객체
     * @return 생성된 일정응답 객체 반환
     */
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @Valid @RequestBody ScheduleRequestDto dto
    ){
        return new ResponseEntity<>(scheduleService.create(dto), HttpStatus.CREATED);
    }

    /**
     * [Controller] 전체 일정 조회 메서드
     * @return 일정응답 객체 리스트 반환
     */
    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules(
    ){
        return new ResponseEntity<>(scheduleService.findAllSchedules(), HttpStatus.OK);
    }

    /**
     * [Controller] 특정 일정 조회 메서드
     * @param id 사용자로부터 받은 일정 id
     * @return 특정 일정응답 객체 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(
            @PathVariable Long id
    ){
        return new ResponseEntity<>(new ScheduleResponseDto(scheduleService.findById(id)), HttpStatus.OK);
    }

    /**
     * [Controller] 특정 일정 수정 메서드
     * @param dto 사용자가 입력한 일정요청 객체
     * @param id 사용자로부터 받은 일정 id
     * @return 수정된 특정 일정응답 객체 반환
     */
    @PostMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @Valid @RequestBody ScheduleRequestDto dto,
            @PathVariable Long id
    ){
        return new ResponseEntity<>(scheduleService.update(dto, id), HttpStatus.OK);
    }

    /**
     * [Controller] 특정 일정 삭제 메서드
     * @param id 사용자로부터 받은 일정 id
     * @return API 응답 객체 반환
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto> deleteSchedule(
            @PathVariable Long id
    ){
        // 프론트에게 응답메시지 보내기 위해 204(NO_CONTENT) 대신 200(OK) 반환
        return new ResponseEntity<>(scheduleService.deleteSchedule(id), HttpStatus.OK);
    }
}
