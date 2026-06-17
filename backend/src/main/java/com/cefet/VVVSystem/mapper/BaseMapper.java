package com.cefet.VVVSystem.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface BaseMapper<E, D> {

    D toDto(E entity);

    E toEntity(D dto);

    default List<D> toDtoList(List<E> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    default List<E> toEntityList(List<D> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    default Set<D> toDtoSet(Set<E> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    default Set<E> toEntitySet(Set<D> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }
}
