package ru.unn.autorepairshop.service;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleService {

    List<LocalDateTime> getAllBusyTimes();

}
