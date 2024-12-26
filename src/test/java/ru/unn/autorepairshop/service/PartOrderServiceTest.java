package ru.unn.autorepairshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.unn.autorepairshop.domain.entity.PartOrder;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.entity.AuthData;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.enums.PartName;
import ru.unn.autorepairshop.domain.enums.Role;
import ru.unn.autorepairshop.repository.PartOrderRepository;
import ru.unn.autorepairshop.service.impl.PartOrderServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartOrderServiceTest {

    @Mock
    private PartOrderRepository partOrderRepository;

    @InjectMocks
    private PartOrderServiceImpl partOrderService;

    private static final String DEFAULT_EMAIL = "test@example.com";
    private static final Long DEFAULT_APPOINTMENT_ID = 1L;
    private static final PartName DEFAULT_PART_NAME = PartName.AIR_FILTER;

    private PartOrder partOrder;

    @BeforeEach
    void setUp() {
        AuthData authData = new AuthData();
        authData.setEmail(DEFAULT_EMAIL);
        authData.setPassword("password");
        authData.setRole(Role.ROLE_CLIENT);

        User client = new User();
        client.setAuthData(authData);

        Appointment appointment = new Appointment();
        appointment.setId(DEFAULT_APPOINTMENT_ID);
        appointment.setClient(client);

        partOrder = new PartOrder();
        partOrder.setPartName(DEFAULT_PART_NAME);
        partOrder.setAppointment(appointment);
    }

    @Test
    void findAllByUserEmailTest() {
        Pageable pageable = PageRequest.of(0, 10);
        when(partOrderRepository.findAllByClientEmail(DEFAULT_EMAIL)).thenReturn(List.of(partOrder));

        Page<PartOrder> partOrders = partOrderService.findAllByUserEmail(DEFAULT_EMAIL, pageable);

        assertNotNull(partOrders);
        assertFalse(partOrders.isEmpty());
        assertEquals(DEFAULT_EMAIL, partOrders.getContent().get(0).getAppointment().getClient().getAuthData().getEmail());
        assertEquals(DEFAULT_PART_NAME, partOrders.getContent().get(0).getPartName());

        verify(partOrderRepository).findAllByClientEmail(DEFAULT_EMAIL);
    }

    @Test
    void findAllByAppointmentIdTest() {
        when(partOrderRepository.findAllByAppointment_Id(DEFAULT_APPOINTMENT_ID)).thenReturn(List.of(partOrder));

        List<PartOrder> partOrders = partOrderService.findAllByAppointmentId(DEFAULT_APPOINTMENT_ID);

        assertNotNull(partOrders);
        assertFalse(partOrders.isEmpty());
        assertEquals(DEFAULT_APPOINTMENT_ID, partOrders.get(0).getAppointment().getId());
        assertEquals(DEFAULT_PART_NAME, partOrders.get(0).getPartName());

        verify(partOrderRepository).findAllByAppointment_Id(DEFAULT_APPOINTMENT_ID);
    }
}
