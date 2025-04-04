package org.example.spartascheduleplus.dto.schedule;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.example.spartascheduleplus.dto.common.PageInfo;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@JsonPropertyOrder({"page", "user", "schedules"})
public class PagedScheduleResponseDto {
    private final PageInfo page;
    private final List<ScheduleResponseDto> schedules;

    // ✅ 생성자 (페이징일정 객체를 받아 생성)
    public PagedScheduleResponseDto(Page<Schedule> pagedSchedule)
    {
        this.page = new PageInfo(pagedSchedule);
        this.schedules = pagedSchedule.map(ScheduleResponseDto::new).getContent();
    }
}
