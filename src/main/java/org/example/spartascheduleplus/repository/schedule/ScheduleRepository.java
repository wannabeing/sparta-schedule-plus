package org.example.spartascheduleplus.repository.schedule;

import org.example.spartascheduleplus.entity.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    /**
     * [Repo] 존재하는지 확인하고, 존재하지 않으면 예외 처리 메서드
     * @param id 존재여부 확인할 id
     * @throws ResponseStatusException 존재하지 않을 경우, 404 예외 처리
     */
    default void existsByIdOrElseThrow(Long id) {
        if (!existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 ID 입니다.");
        }
    }

}
