package com.crudoshlep.islab1.service;

import com.crudoshlep.islab1.model.Discipline;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с дисциплинами
 */
public interface DisciplineService {
    
    /**
     * Создать новую дисциплину
     */
    Discipline createDiscipline(@Valid Discipline discipline);
    
    /**
     * Получить дисциплину по ID
     */
    Optional<Discipline> getDisciplineById(Long id);
    
    /**
     * Обновить дисциплину
     */
    Discipline updateDiscipline(@Valid Discipline discipline);
    
    /**
     * Удалить дисциплину по ID
     */
    boolean deleteDisciplineById(Long id);
    
    /**
     * Получить все дисциплины
     */
    List<Discipline> getAllDisciplines();
    
    /**
     * Получить дисциплины с фильтрацией и пагинацией
     */
    List<Discipline> getDisciplinesWithFilters(String nameFilter, int offset, int limit);
    
    /**
     * Подсчитать общее количество дисциплин
     */
    long getTotalCount();
}
