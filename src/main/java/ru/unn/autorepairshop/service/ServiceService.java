package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.entity.Service;

import java.util.List;

public interface ServiceService {

    void save(Service service);

    List<Service> findAllByVehicleLicensePlate(String licensePlate);

}
