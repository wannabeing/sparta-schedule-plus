package org.example.spartascheduleplus.dto.schedule;

import lombok.Getter;
import org.example.spartascheduleplus.entity.schedule.Schedule;

@Getter
public class ScheduleInfoDto {
    private final Long id;

    private final String title;

    private final String contents;

    // ✅ 생성자 (Schedule 객체를 받아 생성)
    public ScheduleInfoDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
    }
}
