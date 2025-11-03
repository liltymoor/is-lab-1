package com.crudoshlep.islab1.service;

import com.crudoshlep.islab1.model.Discipline;
import com.crudoshlep.islab1.model.Location;
import com.crudoshlep.islab1.model.Person;

import java.util.List;
import java.util.Optional;

public interface LocationService {

    /**
     * Создать новой локации
     */
    Location createLocation(Location person);

    /**
     * Получить локацию по ID
     */
    Optional<Location> getLocationById(Long id);

    /**
     * Обновить локацию
     */
    Location updateLocation(Location person);

    /**
     * Удалить локацию по ID
     */
    boolean deleteLocationById(Long id);

    /**
     * Получить все локации
     */
    List<Location> getAllLocations();

    /**
     * Получить локации с фильтрацией и пагинацией
     */
    List<Location> getLocationsWithFilters(String nameFilter, Double xFilter, Float yFilter, Float zFilter, int offset, int limit);


    /**
     * Подсчитать общее количество локации
     */
    long getTotalCount();}
