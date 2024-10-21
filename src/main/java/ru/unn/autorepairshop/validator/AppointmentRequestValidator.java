package ru.unn.autorepairshop.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.unn.autorepairshop.domain.dto.request.AppointmentCreateRequestDto;
import ru.unn.autorepairshop.domain.entity.Service;
import ru.unn.autorepairshop.domain.entity.User;
import ru.unn.autorepairshop.domain.enums.ServiceType;
import ru.unn.autorepairshop.exceptions.AppointmentException;
import ru.unn.autorepairshop.service.ServiceService;
import ru.unn.autorepairshop.service.UserService;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AppointmentRequestValidator {

    private final UserService userService;

    private final ServiceService serviceService;

    @Autowired
    public AppointmentRequestValidator(UserService userService, ServiceService serviceService) {
        this.userService = userService;
        this.serviceService = serviceService;
    }

    public void validate(AppointmentCreateRequestDto request, String email) {
        Set<User> users = new HashSet<>(userService.getAllByVehicleLicencePlate(request.licensePlate()));
        User user = userService.getByEmail(email);
        Set<ServiceType> uniqueTypes = new HashSet<>(request.serviceTypes());
        Set<Service> servicesByLicencePlate = new HashSet<>(serviceService.findAllByVehicleLicensePlate(request.licensePlate()));
        Set<ServiceType> existingTypes = servicesByLicencePlate
                .stream().map(Service::getServiceType).collect(Collectors.toSet());
        Set<ServiceType> intersection = new HashSet<>(uniqueTypes);
        intersection.retainAll(existingTypes);

        if (!users.contains(user) && users.size() == 1) {
            throw AppointmentException.CODE.CAR_IS_ALREADY_OCCUPIED.get();
        }

        if (uniqueTypes.size() != request.serviceTypes().size()) {
            throw AppointmentException.CODE.REPETITION_OF_SERVICE_TYPES.get();
        }

        if (!intersection.isEmpty()) {
            throw AppointmentException.CODE.SIMILAR_WORKS_EXIST.get();
        }

    }
}
