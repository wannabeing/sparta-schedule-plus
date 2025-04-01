package org.example.spartascheduleplus.entity;


import jakarta.persistence.*;
import lombok.Getter;
import org.example.spartascheduleplus.dto.schedule.ScheduleRequestDto;
import org.example.spartascheduleplus.dto.schedule.ScheduleResponseDto;


@Entity
@Table(name = "schedule")
@Getter
public class Schedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "longtext")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ✅ 기본 생성자
    public Schedule(){}

    // ✅ 생성자 (RequestDTO 객체를 받아 생성)
    public Schedule(ScheduleRequestDto dto){
        this.title = dto.getTitle();
        this.contents = dto.getContents();
    }
    // ✅ 생성자 (ResponseDTO 객체를 받아 생성)
    public Schedule(ScheduleResponseDto dto){
        this.title = dto.getTitle();
        this.contents = dto.getContents();
    }

    /**
     * 🚀 일정 업데이트 메서드
     * @param title 일정 제목
     * @param contents 일정 내용
     */
    public void updateSchedule(String title, String contents){
        this.title = title;
        this.contents = contents;
    }
}
