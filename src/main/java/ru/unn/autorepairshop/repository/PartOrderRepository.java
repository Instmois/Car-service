package ru.unn.autorepairshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.unn.autorepairshop.domain.entity.PartOrder;

import java.util.List;

public interface PartOrderRepository extends JpaRepository<PartOrder, Long> {

    @Query(value = """
                    SELECT po FROM PartOrder po
                    JOIN FETCH po.appointment a
                    JOIN FETCH a.client c 
                    join FETCH c.authData ad
                    WHERE ad.email = :email  
            """)
    List<PartOrder> findAllByClientEmail(String email);

    List<PartOrder> findAllByAppointment_Id(Long id);

}
