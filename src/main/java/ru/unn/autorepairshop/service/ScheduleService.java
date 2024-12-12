package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {

    List<LocalDateTime> getAllBusyTimes();

    void save(Schedule schedule);

}
