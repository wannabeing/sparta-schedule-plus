package org.example.spartascheduleplus.repository.schedule;

import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

}
