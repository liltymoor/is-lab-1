package com.crudoshlep.islab1.service.impl;

import com.crudoshlep.islab1.dao.DisciplineDAO;
import com.crudoshlep.islab1.model.Discipline;
import com.crudoshlep.islab1.service.DisciplineService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с дисциплинами
 */
@Stateless
public class DisciplineServiceImpl implements DisciplineService {
    
    @Inject
    private DisciplineDAO disciplineDAO;
    
    @Override
    public Discipline createDiscipline(@Valid Discipline discipline) {
        return disciplineDAO.save(discipline);
    }
    
    @Override
    public Optional<Discipline> getDisciplineById(Long id) {
        return disciplineDAO.findById(id);
    }
    
    @Override
    public Discipline updateDiscipline(@Valid Discipline discipline) {
        return disciplineDAO.update(discipline);
    }
    
    @Override
    public boolean deleteDisciplineById(Long id) {
        return disciplineDAO.deleteById(id);
    }
    
    @Override
    public List<Discipline> getAllDisciplines() {
        return disciplineDAO.findAll();
    }
    
    @Override
    public List<Discipline> getDisciplinesWithFilters(String nameFilter, int offset, int limit) {
        return disciplineDAO.findWithFilters(nameFilter, offset, limit);
    }
    
    @Override
    public long getTotalCount() {
        return disciplineDAO.count();
    }
}
