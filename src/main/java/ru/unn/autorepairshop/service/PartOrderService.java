package ru.unn.autorepairshop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.unn.autorepairshop.domain.entity.PartOrder;

import java.util.List;

public interface PartOrderService {

    Page<PartOrder> findAllByUserEmail(String email, Pageable pageable);

    List<PartOrder> findAllByAppointmentId(Long appointmentId);

}
