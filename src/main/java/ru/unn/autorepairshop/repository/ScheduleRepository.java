package ru.unn.autorepairshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.unn.autorepairshop.domain.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s.startDate FROM Schedule s")
    List<LocalDateTime> findAllOccupiedStartDates();

}
