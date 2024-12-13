package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.request.AppointmentAddedDateRequestDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentAddedDateResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentAddedMechanicResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentManagerInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.AppointmentShortInfoResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ClientInfoShortResponseDto;
import ru.unn.autorepairshop.domain.dto.response.ManagerViewResponseDto;
import ru.unn.autorepairshop.domain.dto.response.MechanicListResponseDto;
import ru.unn.autorepairshop.domain.dto.response.PartOrderListResponseDto;
import ru.unn.autorepairshop.domain.dto.response.PartOrderResponseDto;
import ru.unn.autorepairshop.domain.entity.Appointment;
import ru.unn.autorepairshop.domain.entity.Mechanic;
import ru.unn.autorepairshop.domain.entity.PartOrder;
import ru.unn.autorepairshop.domain.entity.Schedule;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.enums.AppointmentStatus;
import ru.unn.autorepairshop.domain.mapper.appointment.AppointmentManagerInfoResponseDtoMapper;
import ru.unn.autorepairshop.domain.mapper.partorder.PartOrderResponseDtoMapper;
import ru.unn.autorepairshop.exceptions.AppointmentException;
import ru.unn.autorepairshop.service.AppointmentService;
import ru.unn.autorepairshop.service.ManagerService;
import ru.unn.autorepairshop.service.MechanicService;
import ru.unn.autorepairshop.service.PartOrderService;
import ru.unn.autorepairshop.service.ScheduleService;
import ru.unn.autorepairshop.service.UserService;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ManagerServiceImpl implements ManagerService {

    private final AppointmentService appointmentService;

    private final PartOrderService partOrderService;

    private final MechanicService mechanicService;

    private final UserService userService;

    private final ScheduleService scheduleService;

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

    @Override
    public AppointmentAddedMechanicResponseDto addMechanicToAppointment(String email, Long appointmentId, Long masterId) {
        Appointment appointment = appointmentService.findById(appointmentId);
        User manager = userService.getByEmail(email);
        Schedule schedule = appointment.getSchedule();

        if (schedule == null) {
            schedule = new Schedule();
        }

        Mechanic mechanic = mechanicService.getMechanicById(masterId);

        schedule.setMechanic(mechanic);
        schedule.setClient(manager);
        schedule.setAppointment(appointment);

        checkStatus(appointment, schedule);

        scheduleService.save(schedule);
        appointment.setSchedule(schedule);
        appointmentService.save(appointment);

        return new AppointmentAddedMechanicResponseDto(appointmentId, masterId);
    }

    @Override
    public AppointmentAddedDateResponseDto changeStartDate(
            String email,
            Long appointmentId,
            AppointmentAddedDateRequestDto request
    ) {
        Appointment appointment = appointmentService.findById(appointmentId);
        User manager = userService.getByEmail(email);
        Schedule schedule = appointment.getSchedule();

        if (schedule == null) {
            throw AppointmentException.CODE.MECHANIC_IS_NOT_ASSIGNED.get();
        }

        if (schedule.getEndDate() != null && schedule.getEndDate().isBefore(request.appointmentDate())) {
            throw AppointmentException.CODE.WRONG_DATE.get();
        }

        schedule.setStartDate(request.appointmentDate());
        return getAppointmentAddedDateResponseDto(appointmentId, request, appointment, manager, schedule);
    }

    @Override
    public AppointmentAddedDateResponseDto changeEndDate(String email, Long appointmentId, AppointmentAddedDateRequestDto request) {
        Appointment appointment = appointmentService.findById(appointmentId);
        User manager = userService.getByEmail(email);
        Schedule schedule = appointment.getSchedule();

        if (schedule == null) {
            throw AppointmentException.CODE.MECHANIC_IS_NOT_ASSIGNED.get();
        }

        if (schedule.getStartDate() != null && schedule.getStartDate().isAfter(request.appointmentDate())) {
            throw AppointmentException.CODE.WRONG_DATE.get();
        }

        schedule.setEndDate(request.appointmentDate());
        return getAppointmentAddedDateResponseDto(appointmentId, request, appointment, manager, schedule);
    }

    private AppointmentAddedDateResponseDto getAppointmentAddedDateResponseDto(
            Long appointmentId,
            AppointmentAddedDateRequestDto request,
            Appointment appointment,
            User manager,
            Schedule schedule
    ) {
        schedule.setClient(manager);
        schedule.setAppointment(appointment);

        checkStatus(appointment, schedule);

        scheduleService.save(schedule);
        appointment.setSchedule(schedule);
        appointmentService.save(appointment);

        return new AppointmentAddedDateResponseDto(appointmentId, request.appointmentDate().format(DATE_TIME_FORMATTER));
    }

    private static void checkStatus(Appointment appointment, Schedule schedule) {
        if (appointment.getStatus().equals(AppointmentStatus.NEW)
                && schedule.getStartDate() != null
                && schedule.getEndDate() != null) {
            appointment.setStatus(AppointmentStatus.AT_WORK);
        }
    }


    private ManagerViewResponseDto mapToManagerViewResponseDto(Appointment appointment) {
        AppointmentShortInfoResponseDto appointmentShortInfoResponseDto = new AppointmentShortInfoResponseDto(
                appointment.getServiceType(),
                appointment.getSchedule() != null
                        ? appointment.getSchedule().getMechanic() != null
                        ? appointment.getSchedule().getMechanic().getInitials()
                        : ""
                        : "",
                appointment.getSchedule() != null
                        ? appointment.getSchedule().getStartDate() != null
                        ? appointment.getSchedule().getStartDate().format(DATE_TIME_FORMATTER)
                        : ""
                        : "",
                appointment.getSchedule() != null
                        ? appointment.getSchedule().getEndDate() != null
                        ? appointment.getSchedule().getEndDate().format(DATE_TIME_FORMATTER)
                        : ""
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
