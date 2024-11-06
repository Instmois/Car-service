package ru.unn.autorepairshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.unn.autorepairshop.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAuthData_Email(String email);

    @Query("SELECT u FROM User u " +
            "JOIN u.vehicles v " +
            "WHERE v.licensePlate = :licensePlate"
    )
    List<User> findAllByVehicleLicencePlate(@Param("licensePlate") String licensePlate);

}
