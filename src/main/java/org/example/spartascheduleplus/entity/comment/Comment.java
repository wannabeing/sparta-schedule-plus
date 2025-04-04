package org.example.spartascheduleplus.entity.comment;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.spartascheduleplus.entity.BaseEntity;
import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.example.spartascheduleplus.entity.user.User;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    @Setter
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    public Comment(String comment, Schedule schedule, User user){
        this.comment = comment;
        this.schedule = schedule;
        this.user = user;
    }

    public void updateComment(String comment){
        this.comment = comment;
    }
}
