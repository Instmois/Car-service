package ru.unn.autorepairshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.entity.Vehicle;
import ru.unn.autorepairshop.exceptions.VehicleException;
import ru.unn.autorepairshop.repository.VehicleRepository;
import ru.unn.autorepairshop.service.VehicleService;

import java.util.Optional;

@Service
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicle getByLicensePlate(String licensePlate) {
        return vehicleRepository
                .findByLicensePlate(licensePlate)
                .orElseThrow(VehicleException.CODE.NO_SUCH_VEHICLE_BY_LICENSE_PLATE::get);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicle> getOptionalByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

}
