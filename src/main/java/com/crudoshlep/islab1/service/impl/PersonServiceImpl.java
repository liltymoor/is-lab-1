package com.crudoshlep.islab1.service.impl;

import com.crudoshlep.islab1.dao.PersonDAO;
import com.crudoshlep.islab1.model.Location;
import com.crudoshlep.islab1.model.Person;
import com.crudoshlep.islab1.service.PersonService;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с авторами
 */
@Stateless
@Named("personService")
public class PersonServiceImpl implements PersonService {
    
    @Inject
    private PersonDAO personDAO;
    
    @Override
    public Person createPerson(@Valid Person person) {
        return personDAO.save(person);
    }
    
    @Override
    public Optional<Person> getPersonById(Long id) {
        return personDAO.findById(id);
    }
    
    @Override
    public Person updatePerson(@Valid Person person) {
        return personDAO.update(person);
    }
    
    @Override
    public boolean deletePersonById(Long id) {
        return personDAO.deleteById(id);
    }
    
    @Override
    public List<Person> getAllPersons() {
        return personDAO.findAll();
    }
    
    @Override
    public List<Person> getPersonsWithFilters(String nameFilter, String nationalityFilter, 
                                            String eyeColorFilter, int offset, int limit) {
        return personDAO.findWithFilters(nameFilter, nationalityFilter, eyeColorFilter, offset, limit);
    }
    
    @Override
    public long getTotalCount() {
        return personDAO.count();
    }

    public boolean reassignLocationAndDeletePerson(Long oldPersonId, Long newPersonId) {
        Optional<Person> oldOpt = getPersonById(oldPersonId);
        Optional<Person> newOpt = getPersonById(newPersonId);

        if (oldOpt.isEmpty() || newOpt.isEmpty()) {
            return false;
        }

        Person oldPerson = oldOpt.get();
        Person newPerson = newOpt.get();

        Location locationToMove = oldPerson.getLocation();

        if (locationToMove == null) {
            return deletePersonById(oldPersonId);
        }

        newPerson.setLocation(locationToMove);
        updatePerson(newPerson);
        deletePersonById(oldPersonId);

        return true;
    }

}
