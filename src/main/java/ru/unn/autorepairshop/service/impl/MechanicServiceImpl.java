package ru.unn.autorepairshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.unn.autorepairshop.domain.dto.response.MechanicListResponseDto;
import ru.unn.autorepairshop.domain.mapper.mechanic.MechanicResponseDtoMapper;
import ru.unn.autorepairshop.repository.MechanicRepository;
import ru.unn.autorepairshop.service.MechanicService;

@RequiredArgsConstructor
@Transactional
@Service
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;

    private final MechanicResponseDtoMapper mechanicResponseDtoMapper;

    @Override
    @Transactional(readOnly = true)
    public MechanicListResponseDto getAllMechanics() {
        mechanicRepository.findAll().forEach(System.out::println);
        return new MechanicListResponseDto(mechanicResponseDtoMapper.toDto(mechanicRepository.findAll()));
    }

}
