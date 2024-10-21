package ru.unn.autorepairshop.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.entity.Service;
import ru.unn.autorepairshop.repository.ServiceRepository;
import ru.unn.autorepairshop.service.ServiceService;

import java.util.List;

@org.springframework.stereotype.Service
@Transactional
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public void save(Service service) {
        serviceRepository.save(service);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Service> findAllByVehicleLicensePlate(String licensePlate) {
        return serviceRepository.findAllByVehicleLicensePlateWithNoDoneStatus(licensePlate);
    }

}
