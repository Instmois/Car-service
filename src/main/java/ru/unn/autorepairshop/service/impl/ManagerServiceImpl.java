package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.response.AppointmentManagerInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentShortInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoShortResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ManagerViewResponseDto;
import ru.unn.autorepairshop.domain.dto.response.MechanicListResponseDto;
import ru.unn.autorepairshop.domain.dto.response.PartOrderListResponseDto;
import ru.unn.autorepairshop.domain.dto.response.PartOrderResponseDto;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.entity.PartOrder;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.mapper.appointment.AppointmentManagerInfoResponseDtoMapper;
import ru.unn.autorepairshop.domain.mapper.partorder.PartOrderResponseDtoMapper;
import ru.unn.autorepairshop.service.AppointmentService;
import ru.unn.autorepairshop.service.ManagerService;
import ru.unn.autorepairshop.service.MechanicService;
import ru.unn.autorepairshop.service.PartOrderService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ManagerServiceImpl implements ManagerService {

    private final AppointmentService appointmentService;

    private final PartOrderService partOrderService;

    private final MechanicService mechanicService;

    private final AppointmentManagerInfoResponseDtoMapper appointmentManagerInfoResponseDtoMapper;

    private final PartOrderResponseDtoMapper partOrderResponseDtoMapper;

    private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentManagerInfoResponseDto> getAllAppointments(String client, AppointmentStatus statusFilter, PageRequest of) {
        Page<Appointment> appointments = appointmentService.findAllWithFilter(client, statusFilter, of);
        return appointments.map(appointmentManagerInfoResponseDtoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerViewResponseDto getAppointment(Long id) {
        Appointment appointment = appointmentService.findById(id);
        return mapToManagerViewResponseDto(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public MechanicListResponseDto getAllMechanics() {
        return mechanicService.getAllMechanics();
    }

    private ManagerViewResponseDto mapToManagerViewResponseDto(Appointment appointment) {
        AppointmentShortInfoResponseDto appointmentShortInfoResponseDto = new AppointmentShortInfoResponseDto(
                appointment.getServiceType(),
                appointment.getSchedule() != null
                        ? appointment.getSchedule().getMechanic().getInitials()
                        : "",
                appointment.getSchedule() != null
                        ? appointment.getSchedule().getStartDate().format(DATE_TIME_FORMATTER)
                        : "",
                appointment.getSchedule() != null
                        ? appointment.getSchedule().getEndDate().format(DATE_TIME_FORMATTER)
                        : "",
                appointment.getStatus()
        );

        ClientInfoShortResponseDto clientInfoShortResponseDto = new ClientInfoShortResponseDto(
                createInitials(appointment),
                appointment.getVehicle().getModel(),
                appointment.getClient().getPhoneNumber(),
                appointment.getVehicle().getLicensePlate()
        );

        PartOrderListResponseDto partOrderListResponseDto = new PartOrderListResponseDto(
                createPartOrders(appointment.getId())
        );

        return new ManagerViewResponseDto(
                appointmentShortInfoResponseDto,
                clientInfoShortResponseDto,
                partOrderListResponseDto
        );
    }

    private List<PartOrderResponseDto> createPartOrders(Long id) {
        List<PartOrder> partOrders = partOrderService.findAllByAppointmentId(id);
        return partOrders.stream().map(partOrderResponseDtoMapper::mapPartOrderToDto).toList();
    }

    private String createInitials(Appointment appointment) {
        return appointment.getClient().getLastName() + " "
                + appointment.getClient().getFirstName().charAt(0) + ". "
                + (appointment.getClient().getPatronymic() != null
                ? appointment.getClient().getPatronymic().charAt(0) + "."
                : "");
    }

}
