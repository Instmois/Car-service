package ru.unn.autorepairshop.domain.mapper;

import java.util.List;

public interface EntityResponseMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);

}
