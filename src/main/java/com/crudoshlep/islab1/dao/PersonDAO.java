package com.crudoshlep.islab1.dao;

import com.crudoshlep.islab1.model.Person;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для работы с авторами
 */
@Stateless
@Named("personDao")
public class PersonDAO extends BaseDAO<Person> {
    
    @Inject
    private EntityManager entityManager;
    
    public PersonDAO() {
        super(Person.class);
    }
    
    /**
     * Найти авторов по имени (частичное совпадение)
     */
    public List<Person> findByNameContaining(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
        
        Predicate namePredicate = cb.like(cb.lower(root.get("name")), 
                                        "%" + name.toLowerCase() + "%");
        cq.where(namePredicate);
        
        TypedQuery<Person> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Найти авторов по национальности
     */
    public List<Person> findByNationality(String nationality) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
        
        cq.where(cb.equal(root.get("nationality"), nationality));
        
        TypedQuery<Person> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Найти авторов по цвету глаз
     */
    public List<Person> findByEyeColor(String eyeColor) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
        
        cq.where(cb.equal(root.get("eyeColor"), eyeColor));
        
        TypedQuery<Person> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Найти авторов с фильтрацией
     */
    public List<Person> findWithFilters(String nameFilter, String nationalityFilter, 
                                      String eyeColorFilter, int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (nameFilter != null && !nameFilter.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), 
                                 "%" + nameFilter.toLowerCase() + "%"));
        }
        
        if (nationalityFilter != null && !nationalityFilter.trim().isEmpty()) {
            predicates.add(cb.equal(root.get("nationality"), nationalityFilter));
        }
        
        if (eyeColorFilter != null && !eyeColorFilter.trim().isEmpty()) {
            predicates.add(cb.equal(root.get("eyeColor"), eyeColorFilter));
        }
        
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        cq.orderBy(cb.asc(root.get("id")));
        
        TypedQuery<Person> query = entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        
        return query.getResultList();
    }
}
