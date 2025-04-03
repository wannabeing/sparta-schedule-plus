package org.example.spartascheduleplus.entity.schedule;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.spartascheduleplus.dto.schedule.ScheduleRequestDto;
import org.example.spartascheduleplus.entity.BaseEntity;
import org.example.spartascheduleplus.entity.user.User;


@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @Setter
    private User user;

    // ✅ 생성자 (RequestDTO 객체를 받아 생성)
    public Schedule(ScheduleRequestDto dto, User user){
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.user = user;
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
