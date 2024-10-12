package ru.unn.autorepairshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unn.autorepairshop.domain.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAuthData_Email(String email);

}
