package com.crudoshlep.islab1.service.impl;

import com.crudoshlep.islab1.dao.LabWorkDAO;
import com.crudoshlep.islab1.model.Difficulty;
import com.crudoshlep.islab1.model.LabWork;
import com.crudoshlep.islab1.service.LabWorkService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.inject.Named;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с лабораторными работами
 */
@Stateless
@Named("labWorkService")
public class LabWorkServiceImpl implements LabWorkService {
    
    @Inject
    private LabWorkDAO labWorkDAO;
    
    @Override
    public LabWork createLabWork(@Valid LabWork labWork) {
        LabWork created = labWorkDAO.save(labWork);
        // notify others that labwork created
        return created;
    }
    
    @Override
    public Optional<LabWork> getLabWorkById(Integer id) {
        return labWorkDAO.findByIdWithAll(id);
    }
    
    @Override
    public LabWork updateLabWork(@Valid LabWork labWork) {
        LabWork updated = labWorkDAO.update(labWork);
        // notify others
        return updated;
    }
    
    @Override
    public boolean deleteLabWorkById(Integer id) {
        Optional<LabWork> labWorkOpt = labWorkDAO.findById(id);
        if (labWorkOpt.isPresent()) {
            boolean deleted = labWorkDAO.deleteById(id);
            if (deleted) {
                // notify others
            }
            return deleted;
        }
        return false;
    }
    
    @Override
    public List<LabWork> getAllLabWorks() {
        return labWorkDAO.findAll();
    }
    
    @Override
    public List<LabWork> getLabWorksWithPagination(int offset, int limit) {
        return labWorkDAO.findWithPagination(offset, limit);
    }
    
    @Override
    public List<LabWork> getLabWorksWithFilters(String nameFilter, String difficultyFilter, 
                                               Integer minimalPointFilter, int offset, int limit) {
        return labWorkDAO.findWithFilters(nameFilter, difficultyFilter, minimalPointFilter, offset, limit);
    }
    
    @Override
    public long getTotalCount() {
        return labWorkDAO.count();
    }
    
    @Override
    public long getCountWithFilters(String nameFilter, String difficultyFilter, 
                                  Integer minimalPointFilter) {
        return labWorkDAO.countWithFilters(nameFilter, difficultyFilter, minimalPointFilter);
    }
    
    @Override
    public int deleteByMinimalPoint(int minimalPoint) {
        int deletedCount = labWorkDAO.deleteByMinimalPoint(minimalPoint);
        if (deletedCount > 0) {
            // notify others
        }
        return deletedCount;
    }
    
    @Override
    public long countByPersonalQualitiesMaximum(Float personalQualitiesMaximum) {
        return labWorkDAO.countByPersonalQualitiesMaximum(personalQualitiesMaximum);
    }
    
    @Override
    public long countByAuthorLessThan(Long authorId) {
        return labWorkDAO.countByAuthorLessThan(authorId);
    }
    
    @Override
    public boolean increaseDifficulty(Integer labWorkId, int steps) {
        Optional<LabWork> labWorkOpt = labWorkDAO.findById(labWorkId);
        if (labWorkOpt.isPresent()) {
            LabWork labWork = labWorkOpt.get();
            Difficulty currentDifficulty = labWork.getDifficulty();
            
            if (currentDifficulty != null) {
                Difficulty newDifficulty = increaseDifficultyBySteps(currentDifficulty, steps);
                labWork.setDifficulty(newDifficulty);
                labWorkDAO.update(labWork);
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean decreaseDifficulty(Integer labWorkId, int steps) {
        Optional<LabWork> labWorkOpt = labWorkDAO.findById(labWorkId);
        if (labWorkOpt.isPresent()) {
            LabWork labWork = labWorkOpt.get();
            Difficulty currentDifficulty = labWork.getDifficulty();
            
            if (currentDifficulty != null) {
                Difficulty newDifficulty = decreaseDifficultyBySteps(currentDifficulty, steps);
                labWork.setDifficulty(newDifficulty);
                labWorkDAO.update(labWork);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Повысить сложность на указанное количество шагов
     */
    private Difficulty increaseDifficultyBySteps(Difficulty current, int steps) {
        Difficulty[] difficulties = Difficulty.values();
        int currentIndex = -1;
        
        for (int i = 0; i < difficulties.length; i++) {
            if (difficulties[i] == current) {
                currentIndex = i;
                break;
            }
        }
        
        if (currentIndex == -1) {
            return current;
        }
        
        int newIndex = Math.min(currentIndex + steps, difficulties.length - 1);
        return difficulties[newIndex];
    }
    
    /**
     * Понизить сложность на указанное количество шагов
     */
    private Difficulty decreaseDifficultyBySteps(Difficulty current, int steps) {
        Difficulty[] difficulties = Difficulty.values();
        int currentIndex = -1;
        
        for (int i = 0; i < difficulties.length; i++) {
            if (difficulties[i] == current) {
                currentIndex = i;
                break;
            }
        }
        
        if (currentIndex == -1) {
            return current;
        }
        
        int newIndex = Math.max(currentIndex - steps, 0);
        return difficulties[newIndex];
    }
}
