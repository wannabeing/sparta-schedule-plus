package org.example.spartascheduleplus.controller.schedule;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.spartascheduleplus.dto.api.SuccessResponseDto;
import org.example.spartascheduleplus.dto.schedule.PagedScheduleResponseDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleDetailResponseDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleRequestDto;
import org.example.spartascheduleplus.dto.user.UserResponseDto;
import org.example.spartascheduleplus.service.schedule.ScheduleService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.spartascheduleplus.dto.api.SuccessResponseDto.createSuccessResponseDto;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * [Controller] 일정 생성 메서드
     * @param requestDto 사용자가 입력한 일정요청 객체
     * @param loginUser 세션에 저장된 유저 객체
     * @param httpRequest Http 요청 객체
     * @return 생성된 상세일정 응답 객체 반환
     */
    @PostMapping("/create")
    public ResponseEntity<SuccessResponseDto<ScheduleDetailResponseDto>> createSchedule(
            @Valid @RequestBody ScheduleRequestDto requestDto,
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                createSuccessResponseDto(
                        HttpStatus.CREATED,
                        httpRequest.getRequestURI(),
                        "일정 생성에 성공하였습니다",
                        scheduleService.createSchedule(
                                requestDto,
                                loginUser.getId())
                ),
                HttpStatus.CREATED
        );
    }

    /**
     * [Controller] 전체 일정 조회 메서드
     * @param httpRequest Http 요청 객체
     * @return 페이징일정 객체를 반환
     */
    @GetMapping
    public ResponseEntity<SuccessResponseDto<PagedScheduleResponseDto>> findAllSchedules(
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                createSuccessResponseDto(
                        HttpStatus.OK,
                        httpRequest.getRequestURI(),
                        "전체 일정을 조회합니다",
                        scheduleService.findAllSchedules(pageable)
                ),
                HttpStatus.OK
        );
    }

    /**
     * [Controller] 특정 일정 조회 메서드
     * @param id 사용자로부터 받은 일정 id
     * @param httpRequest Http 요청 객체
     * @return 상세일정 응답 객체 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponseDto<ScheduleDetailResponseDto>> findScheduleById(
            @PathVariable Long id,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                createSuccessResponseDto(
                        HttpStatus.OK,
                        httpRequest.getRequestURI(),
                        "단일 일정을 조회합니다",
                        scheduleService.createDetailScheduleDto(id)
                ),
                HttpStatus.OK
        );
    }

    /**
     * [Controller] 특정 일정 수정 메서드
     * @param dto 사용자가 입력한 일정요청 객체
     * @param id 사용자로부터 받은 일정 id
     * @param httpRequest Http 요청 객체
     * @return 수정된 상세일정 응답 객체 반환
     */
    @PostMapping("/{id}")
    public ResponseEntity<SuccessResponseDto<ScheduleDetailResponseDto>> updateSchedule(
            @Valid @RequestBody ScheduleRequestDto dto,
            @PathVariable Long id,
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser,
            HttpServletRequest httpRequest)
    {
        return new ResponseEntity<>(
                createSuccessResponseDto(
                        HttpStatus.OK,
                        httpRequest.getRequestURI(),
                        "일정 수정을 완료했습니다.",
                        scheduleService.updateSchedule(dto, id, loginUser.getId())
                ),
                HttpStatus.OK
        );

    }

    /**
     * [Controller] 특정 일정 삭제 메서드
     * @param id 사용자로부터 받은 일정 id
     * @param httpRequest Http 요청 객체
     * @return API 응답 객체 반환
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponseDto> deleteSchedule(
            @PathVariable Long id,
            @SessionAttribute(
                    name = "loginUser")
            UserResponseDto loginUser,
            HttpServletRequest httpRequest
    ){
        // 특정 일정 삭제
        scheduleService.deleteSchedule(id, loginUser.getId());

        return new ResponseEntity<>(
                createSuccessResponseDto(
                        HttpStatus.OK,
                        httpRequest.getRequestURI(),
                        "요청한 일정을 성공적으로 삭제했습니다.",
                        null
                ),
                HttpStatus.OK
        );
    }
}
