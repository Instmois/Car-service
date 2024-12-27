package ru.unn.autorepairshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.unn.autorepairshop.domain.entity.Mechanic;
import ru.unn.autorepairshop.repository.MechanicRepository;
import ru.unn.autorepairshop.service.impl.MechanicServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MechanicServiceTest {

    @Mock
    private MechanicRepository mechanicRepository;

    @InjectMocks
    private MechanicServiceImpl mechanicService;

    private static final Long DEFAULT_MECHANIC_ID = 1L;

    private Mechanic mechanic;

    @BeforeEach
    void setUp() {
        mechanic = Mechanic.builder()
                .id(DEFAULT_MECHANIC_ID)
                .initials("Иванов И.И.")
                .schedules(List.of())
                .build();
    }

    @Test
    void getMechanicById() {
        when(mechanicRepository.findById(DEFAULT_MECHANIC_ID)).thenReturn(Optional.of(mechanic));

        Mechanic result = mechanicService.getMechanicById(DEFAULT_MECHANIC_ID);

        assertNotNull(result);
        assertEquals(DEFAULT_MECHANIC_ID, result.getId());
        assertEquals("Иванов И.И.", result.getInitials());
        verify(mechanicRepository).findById(DEFAULT_MECHANIC_ID);
    }

    @Test
    void getMechanicById_returnsNull_ifNotFound() {
        when(mechanicRepository.findById(DEFAULT_MECHANIC_ID)).thenReturn(Optional.empty());

        Mechanic result = mechanicService.getMechanicById(DEFAULT_MECHANIC_ID);

        assertNull(result);
        verify(mechanicRepository).findById(DEFAULT_MECHANIC_ID);
    }
}
