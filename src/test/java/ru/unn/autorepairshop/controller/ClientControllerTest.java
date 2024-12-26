package ru.unn.autorepairshop.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import ru.unn.autorepairshop.config.SecurityConfiguration;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientInfoUpdateRequestDto;
import ru.unn.autorepairshop.domain.dto.request.ClientUpdatePasswordRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentAllInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentCreatedResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentDateResponseDto;
import ru.unn.autorepairshop.domain.dto.response.BusyDaysResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoUpdateResponseDto;
import ru.unn.autorepairshop.domain.dto.response.PartOrderResponseDto;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.enums.ServiceType;
import ru.unn.autorepairshop.facade.AppointmentFacade;
import ru.unn.autorepairshop.security.jwt.JwtAuthEntryPoint;
import ru.unn.autorepairshop.security.jwt.JwtCore;
import ru.unn.autorepairshop.security.service.UserDetailsServiceImpl;
import ru.unn.autorepairshop.service.ClientService;

@WebMvcTest(ClientController.class)
@Import(SecurityConfiguration.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    protected JwtCore jwtCore;

    @MockBean
    protected JwtAuthEntryPoint jwtAuthEntryPoint;

    @MockBean
    private ClientService clientService;

    @MockBean
    protected UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    private AppointmentFacade appointmentFacade;

    private static final String PATH = "/api/v1/client";
    private static final String CONTENT_TYPE = "application/json";
    private AppointmentCreateRequestDto appointmentCreateRequest;
    private ClientInfoUpdateRequestDto clientInfoUpdateRequest;
    private ClientUpdatePasswordRequestDto clientUpdatePasswordRequest;

    private AppointmentCreatedResponseDto appointmentCreatedResponse;
    private AppointmentDateResponseDto appointmentDateResponse;
    private BusyDaysResponseDto busyDaysResponse;
    private ClientInfoResponseDto clientInfoResponse;
    private ClientInfoUpdateResponseDto clientInfoUpdateResponse;

    @BeforeEach
    void setUp() {
        appointmentCreateRequest = new AppointmentCreateRequestDto(
                "A000AA", "Toyota Camry",
                LocalDateTime.of(2025, 11, 1, 14, 30, 0),
                ServiceType.REPAIR);

        appointmentCreatedResponse = new AppointmentCreatedResponseDto(
                "A000AA", "Toyota Camry",
                AppointmentStatus.NEW,
                LocalDateTime.of(2025, 11, 1, 14, 30, 0),
                ServiceType.REPAIR);

        appointmentDateResponse = new AppointmentDateResponseDto("2024-11-01T14:30:00");

        busyDaysResponse = new BusyDaysResponseDto(
                List.of(LocalDateTime.of(2025, 11, 6, 10, 0, 0),
                        LocalDateTime.of(2025, 11, 6, 11, 0, 0))
        );

        clientInfoResponse = new ClientInfoResponseDto("Иван", "Иванов", "Иванович", "88005553535", "test@example.com");
        clientInfoUpdateResponse = new ClientInfoUpdateResponseDto("Иван", "Иванов", "Иванович", "88005553535", "test@example.com");

        clientInfoUpdateRequest = new ClientInfoUpdateRequestDto("Иван", "Иванов", "Иванович", "88005553535", "test@example.com");
        clientUpdatePasswordRequest = new ClientUpdatePasswordRequestDto("oldPassword", "newPassword", "newPassword");
    }

    // Appointment creation test
    @Test
    void createAppointmentTest() throws Exception {
        when(appointmentFacade.createAppointment(any(AppointmentCreateRequestDto.class), any(String.class)))
                .thenReturn(appointmentCreatedResponse);

        mockMvc.perform(post(PATH + "/appointment")
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(appointmentCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(appointmentCreatedResponse)));
    }

    // Get all appointments test
    @Test
    void getAllAppointmentsTest() throws Exception {
        Page<AppointmentAllInfoResponseDto> page = Page.empty(PageRequest.of(0, 6));

        when(clientService.getAllAppointments(any(), any()))
                .thenReturn(page);

        mockMvc.perform(get(PATH + "/appointments")
                        .param("page", "0")
                        .param("size", "6"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    // Get appointment date test
    @Test
    void getAppointmentDateTest() throws Exception {
        when(clientService.getAppointmentDate(any(Long.class)))
                .thenReturn(appointmentDateResponse);

        mockMvc.perform(get(PATH + "/appointment/1/date"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(appointmentDateResponse)));
    }

    // Get busy days test
    @Test
    void getAllBusyDaysTest() throws Exception {
        when(clientService.getAllBusyDays())
                .thenReturn(busyDaysResponse);

        mockMvc.perform(get(PATH + "/busy-days"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(busyDaysResponse)));
    }

    // Get current client info test
    @Test
    void getCurrentClientTest() throws Exception {
        when(clientService.getInfoAboutCurrentUser(any(String.class)))
                .thenReturn(clientInfoResponse);

        mockMvc.perform(get(PATH + "/current"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(clientInfoResponse)));
    }

    // Update current client info test
    @Test
    void updateCurrentClientTest() throws Exception {
        when(clientService.updateInfoAboutCurrentUser(any(ClientInfoUpdateRequestDto.class), any(String.class)))
                .thenReturn(clientInfoUpdateResponse);

        mockMvc.perform(put(PATH + "/current")
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(clientInfoUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(clientInfoUpdateResponse)));
    }

    // Get all part orders test
    @Test
    void getAllPartOrdersTest() throws Exception {
        Page<PartOrderResponseDto> page = Page.empty(PageRequest.of(0, 6));

        when(clientService.getAllPartOrders(any(), any()))
                .thenReturn(page);

        mockMvc.perform(get(PATH + "/part-orders")
                        .param("page", "0")
                        .param("size", "6"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    // Update password test
    @Test
    void updatePasswordTest() throws Exception {
        when(clientService.updatePassword(any(ClientUpdatePasswordRequestDto.class), any(String.class)))
                .thenReturn(null);

        mockMvc.perform(put(PATH + "/password")
                        .contentType(CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(clientUpdatePasswordRequest)))
                .andExpect(status().isOk());
    }
}
