package org.example.spartascheduleplus.dto.schedule;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.example.spartascheduleplus.dto.common.PageInfo;

import java.util.List;

@Getter
@JsonPropertyOrder({"page", "user", "schedules"})
public class PagedScheduleResponseDto {
    private final PageInfo page;
    private final List<ScheduleResponseDto> schedules;

    // ✅ 생성자 (페이징일정 객체를 받아 생성)
    public PagedScheduleResponseDto(
            PageInfo page,
            List<ScheduleResponseDto> schedules)
    {
        this.page = page;
        this.schedules = schedules;
    }
}
