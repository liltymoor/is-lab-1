package com.crudoshlep.islab1.dao;

import com.crudoshlep.islab1.model.Discipline;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для работы с дисциплинами
 */
@Stateless
public class DisciplineDAO extends BaseDAO<Discipline> {
    
    @jakarta.inject.Inject
    private EntityManager entityManager;
    
    public DisciplineDAO() {
        super(Discipline.class);
    }
    
    /**
     * Найти дисциплины по имени (частичное совпадение)
     */
    public List<Discipline> findByNameContaining(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Discipline> cq = cb.createQuery(Discipline.class);
        Root<Discipline> root = cq.from(Discipline.class);
        
        Predicate namePredicate = cb.like(cb.lower(root.get("name")), 
                                        "%" + name.toLowerCase() + "%");
        cq.where(namePredicate);
        
        TypedQuery<Discipline> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Найти дисциплины с фильтрацией
     */
    public List<Discipline> findWithFilters(String nameFilter, int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Discipline> cq = cb.createQuery(Discipline.class);
        Root<Discipline> root = cq.from(Discipline.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (nameFilter != null && !nameFilter.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), 
                                 "%" + nameFilter.toLowerCase() + "%"));
        }
        
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        cq.orderBy(cb.asc(root.get("id")));
        
        TypedQuery<Discipline> query = entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        
        return query.getResultList();
    }
}
