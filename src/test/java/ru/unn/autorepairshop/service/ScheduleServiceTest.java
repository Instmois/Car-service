package ru.unn.autorepairshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.entity.Mechanic;
import ru.unn.autorepairshop.domain.entity.Schedule;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.entity.AuthData;
import ru.unn.autorepairshop.domain.enums.Role;
import ru.unn.autorepairshop.repository.ScheduleRepository;
import ru.unn.autorepairshop.service.impl.ScheduleServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final Long DEFAULT_SCHEDULE_ID = 1L;
    private static final Long DEFAULT_APPOINTMENT_ID = 2L;
    private static final LocalDateTime DEFAULT_START_DATE = LocalDateTime.of(2024, 12, 31, 10, 0);
    private static final LocalDateTime DEFAULT_END_DATE = LocalDateTime.of(2024, 12, 31, 12, 0);

    private Schedule schedule;
    private User client;
    private Mechanic mechanic;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        AuthData authData = new AuthData();
        authData.setEmail(DEFAULT_EMAIL);
        authData.setPassword("password");
        authData.setRole(Role.ROLE_CLIENT);

        client = new User();
        client.setAuthData(authData);

        mechanic = new Mechanic();
        mechanic.setId(1L);
        mechanic.setInitials("Иванов И.И.");

        appointment = new Appointment();
        appointment.setId(DEFAULT_APPOINTMENT_ID);
        appointment.setClient(client);

        schedule = Schedule.builder()
                .id(DEFAULT_SCHEDULE_ID)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE)
                .appointment(appointment)
                .client(client)
                .mechanic(mechanic)
                .build();
    }

    @Test
    void getAllBusyTimesTest() {
        when(scheduleRepository.findAllOccupiedStartDates()).thenReturn(List.of(DEFAULT_START_DATE));

        List<LocalDateTime> busyTimes = scheduleService.getAllBusyTimes();

        assertNotNull(busyTimes);
        assertFalse(busyTimes.isEmpty());
        assertEquals(DEFAULT_START_DATE, busyTimes.get(0));

        verify(scheduleRepository).findAllOccupiedStartDates();
    }

    @Test
    void saveTest() {
        when(scheduleRepository.saveAndFlush(schedule)).thenReturn(schedule);

        Schedule savedSchedule = scheduleService.save(schedule);

        assertNotNull(savedSchedule);
        assertEquals(DEFAULT_SCHEDULE_ID, savedSchedule.getId());
        assertEquals(DEFAULT_START_DATE, savedSchedule.getStartDate());
        assertEquals(DEFAULT_END_DATE, savedSchedule.getEndDate());
        assertEquals(client, savedSchedule.getClient());
        assertEquals(appointment, savedSchedule.getAppointment());
        assertEquals(mechanic, savedSchedule.getMechanic());

        verify(scheduleRepository).saveAndFlush(schedule);
    }
}
