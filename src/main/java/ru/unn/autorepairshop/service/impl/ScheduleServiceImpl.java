package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.entity.Schedule;
import ru.unn.autorepairshop.repository.ScheduleRepository;
import ru.unn.autorepairshop.service.ScheduleService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<LocalDateTime> getAllBusyTimes() {
        return scheduleRepository.findAllOccupiedStartDates();
    }

    @Override
    public Schedule save(Schedule schedule) {
        return scheduleRepository.saveAndFlush(schedule);
    }

    @Override
    public void delete(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }

}
