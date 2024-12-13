package ru.unn.autorepairshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.unn.autorepairshop.domain.entity.Mechanic;

public interface MechanicRepository extends JpaRepository<Mechanic, Long> {
}
