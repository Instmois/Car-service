package ru.unn.autorepairshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.unn.autorepairshop.domain.entity.Service;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Query("SELECT s FROM Service s " +
            "JOIN s.appointment a " +
            "JOIN a.vehicle v " +
            "WHERE v.licensePlate = :licensePlate " +
            "AND s.serviceStatus != ru.unn.autorepairshop.domain.enums.ServiceStatus.DONE")
    List<Service> findAllByVehicleLicensePlateWithNoDoneStatus(@Param("licensePlate") String licensePlate);

}
