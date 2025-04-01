package org.example.spartascheduleplus.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.example.spartascheduleplus.entity.Schedule;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {
    private final Long id;

    // FIXME: userId 추가 예정
    // private final Long userId;

    private final String title;

    private final String contents;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime updatedAt;

    // ✅ 생성자 (Schedule 객체를 받아 생성)
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}
