package ru.unn.autorepairshop.service;

import ru.unn.autorepairshop.domain.dto.response.MechanicListResponseDto;
import ru.unn.autorepairshop.domain.entity.Mechanic;

public interface MechanicService {

    MechanicListResponseDto getAllMechanics();

    Mechanic getMechanicById(Long id);

}
