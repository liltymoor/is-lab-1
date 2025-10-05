package com.crudoshlep.islab1.service;

import com.crudoshlep.islab1.model.Person;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с авторами
 */
public interface PersonService {
    
    /**
     * Создать нового автора
     */
    Person createPerson(Person person);
    
    /**
     * Получить автора по ID
     */
    Optional<Person> getPersonById(Long id);
    
    /**
     * Обновить автора
     */
    Person updatePerson(Person person);
    
    /**
     * Удалить автора по ID
     */
    boolean deletePersonById(Long id);
    
    /**
     * Получить всех авторов
     */
    List<Person> getAllPersons();
    
    /**
     * Получить авторов с фильтрацией и пагинацией
     */
    List<Person> getPersonsWithFilters(String nameFilter, String nationalityFilter, 
                                     String eyeColorFilter, int offset, int limit);
    
    /**
     * Подсчитать общее количество авторов
     */
    long getTotalCount();
}
