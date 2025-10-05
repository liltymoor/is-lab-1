package com.crudoshlep.islab1.service;

import com.crudoshlep.islab1.model.LabWork;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с лабораторными работами
 */
public interface LabWorkService {
    
    /**
     * Создать новую лабораторную работу
     */
    LabWork createLabWork(LabWork labWork);
    
    /**
     * Получить лабораторную работу по ID
     */
    Optional<LabWork> getLabWorkById(Integer id);
    
    /**
     * Обновить лабораторную работу
     */
    LabWork updateLabWork(LabWork labWork);
    
    /**
     * Удалить лабораторную работу по ID
     */
    boolean deleteLabWorkById(Integer id);
    
    /**
     * Получить все лабораторные работы
     */
    List<LabWork> getAllLabWorks();
    
    /**
     * Получить лабораторные работы с пагинацией
     */
    List<LabWork> getLabWorksWithPagination(int offset, int limit);
    
    /**
     * Получить лабораторные работы с фильтрацией и пагинацией
     */
    List<LabWork> getLabWorksWithFilters(String nameFilter, String difficultyFilter, 
                                       Integer minimalPointFilter, int offset, int limit);
    
    /**
     * Подсчитать общее количество лабораторных работ
     */
    long getTotalCount();
    
    /**
     * Подсчитать количество лабораторных работ с фильтрацией
     */
    long getCountWithFilters(String nameFilter, String difficultyFilter, 
                           Integer minimalPointFilter);
    
    // Специальные операции
    
    /**
     * Удалить все лабораторные работы с определенным минимальным количеством баллов
     */
    int deleteByMinimalPoint(int minimalPoint);
    
    /**
     * Подсчитать количество лабораторных работ с определенным максимальным количеством баллов за личные качества
     */
    long countByPersonalQualitiesMaximum(Float personalQualitiesMaximum);
    
    /**
     * Подсчитать количество лабораторных работ с автором меньше заданного
     */
    long countByAuthorLessThan(Long authorId);
    
    /**
     * Повысить сложность лабораторной работы на указанное число шагов
     */
    boolean increaseDifficulty(Integer labWorkId, int steps);
    
    /**
     * Понизить сложность лабораторной работы на указанное число шагов
     */
    boolean decreaseDifficulty(Integer labWorkId, int steps);
}
