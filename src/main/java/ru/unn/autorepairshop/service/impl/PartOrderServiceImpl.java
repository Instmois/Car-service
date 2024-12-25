package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.entity.PartOrder;
import ru.unn.autorepairshop.repository.PartOrderRepository;
import ru.unn.autorepairshop.service.PartOrderService;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class PartOrderServiceImpl implements PartOrderService {

    private final PartOrderRepository partOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<PartOrder> findAllByUserEmail(String email, Pageable pageable) {
        List<PartOrder> result = partOrderRepository.findAllByClientEmail(email);
        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartOrder> findAllByAppointmentId(Long appointmentId) {
        return partOrderRepository.findAllByAppointment_Id(appointmentId);
    }

    @Override
    public void save(PartOrder partOrder) {
        partOrderRepository.save(partOrder);
    }

    @Override
    public void delete(Long orderId) {
        partOrderRepository.delete(this.getById(orderId));
    }

    @Override
    @Transactional(readOnly = true)
    public PartOrder findById(Long orderId) {
        return this.getById(orderId);
    }

    private PartOrder getById(Long orderId) {
        return partOrderRepository.findById(orderId).orElseThrow();
    }

}
