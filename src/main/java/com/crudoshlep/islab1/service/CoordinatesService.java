package com.crudoshlep.islab1.service;

import com.crudoshlep.islab1.model.Coordinates;
import com.crudoshlep.islab1.model.Location;

import java.util.List;
import java.util.Optional;

public interface CoordinatesService {
    /**
     * Создать новой координаты
     */
    Coordinates createCoordinates(Coordinates person);

    /**
     * Получить координаты по ID
     */
    Optional<Coordinates> getCoordinatesById(Long id);

    /**
     * Обновить координаты
     */
    Coordinates updateCoordinates(Coordinates person);

    /**
     * Удалить координаты по ID
     */
    boolean deleteCoordinatesById(Long id);

    /**
     * Получить все координаты
     */
    List<Coordinates> getAllCoordinates();

    /**
     * Получить координаты с фильтрацией и пагинацией
     */
    List<Coordinates> getCoordinatesWithFilters(Integer xFilter, Float yFilter, int offset, int limit);


    /**
     * Подсчитать общее количество коордиант
     */
    long getTotalCount();
}
