package com.crudoshlep.islab1.dao;

import com.crudoshlep.islab1.model.LabWork;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO для работы с лабораторными работами
 */
@Stateless
public class LabWorkDAO extends BaseDAO<LabWork> {
    
    @jakarta.inject.Inject
    private EntityManager entityManager;
    
    public LabWorkDAO() {
        super(LabWork.class);
    }
    
    /**
     * Найти лабораторную работу по ID с подгрузкой зависимостей
     * coordinates, discipline, author и его location
     */
    public java.util.Optional<LabWork> findByIdWithAll(Integer id) {
        TypedQuery<LabWork> query = entityManager.createQuery(
                "SELECT lw FROM LabWork lw " +
                        "LEFT JOIN FETCH lw.coordinates " +
                        "LEFT JOIN FETCH lw.discipline " +
                        "LEFT JOIN FETCH lw.author a " +
                        "LEFT JOIN FETCH a.location " +
                        "WHERE lw.id = :id", LabWork.class);
        query.setParameter("id", id);
        java.util.List<LabWork> result = query.getResultList();
        return result.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(result.get(0));
    }
    
    /**
     * Найти лабораторные работы по имени (частичное совпадение)
     */
    public List<LabWork> findByNameContaining(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LabWork> cq = cb.createQuery(LabWork.class);
        Root<LabWork> root = cq.from(LabWork.class);
        
        Predicate namePredicate = cb.like(cb.lower(root.get("name")), 
                                        "%" + name.toLowerCase() + "%");
        cq.where(namePredicate);
        
        TypedQuery<LabWork> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Найти лабораторные работы по минимальному количеству баллов
     */
    public List<LabWork> findByMinimalPoint(int minimalPoint) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LabWork> cq = cb.createQuery(LabWork.class);
        Root<LabWork> root = cq.from(LabWork.class);
        
        cq.where(cb.equal(root.get("minimalPoint"), minimalPoint));
        
        TypedQuery<LabWork> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Найти лабораторные работы по максимальному количеству баллов за личные качества
     */
    public List<LabWork> findByPersonalQualitiesMaximum(Float personalQualitiesMaximum) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LabWork> cq = cb.createQuery(LabWork.class);
        Root<LabWork> root = cq.from(LabWork.class);
        
        cq.where(cb.equal(root.get("personalQualitiesMaximum"), personalQualitiesMaximum));
        
        TypedQuery<LabWork> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Найти лабораторные работы по автору
     */
    public List<LabWork> findByAuthorId(Long authorId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LabWork> cq = cb.createQuery(LabWork.class);
        Root<LabWork> root = cq.from(LabWork.class);
        
        cq.where(cb.equal(root.get("author").get("id"), authorId));
        
        TypedQuery<LabWork> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Найти лабораторные работы по сложности
     */
    public List<LabWork> findByDifficulty(String difficulty) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LabWork> cq = cb.createQuery(LabWork.class);
        Root<LabWork> root = cq.from(LabWork.class);
        
        cq.where(cb.equal(root.get("difficulty"), difficulty));
        
        TypedQuery<LabWork> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Найти лабораторные работы с пагинацией
     */
    public List<LabWork> findWithPagination(int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LabWork> cq = cb.createQuery(LabWork.class);
        Root<LabWork> root = cq.from(LabWork.class);
        cq.select(root);
        cq.orderBy(cb.asc(root.get("id")));
        
        TypedQuery<LabWork> query = entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        
        return query.getResultList();
    }
    
    /**
     * Найти лабораторные работы с фильтрацией и пагинацией
     */
    public List<LabWork> findWithFilters(String nameFilter, String difficultyFilter, 
                                       Integer minimalPointFilter, int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LabWork> cq = cb.createQuery(LabWork.class);
        Root<LabWork> root = cq.from(LabWork.class);
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (nameFilter != null && !nameFilter.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), 
                                 "%" + nameFilter.toLowerCase() + "%"));
        }
        
        if (difficultyFilter != null && !difficultyFilter.trim().isEmpty()) {
            predicates.add(cb.equal(root.get("difficulty"), difficultyFilter));
        }
        
        if (minimalPointFilter != null) {
            predicates.add(cb.equal(root.get("minimalPoint"), minimalPointFilter));
        }
        
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        cq.orderBy(cb.asc(root.get("id")));
        
        TypedQuery<LabWork> query = entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        
        return query.getResultList();
    }
    
    /**
     * Подсчитать количество лабораторных работ с фильтрацией
     */
    public long countWithFilters(String nameFilter, String difficultyFilter, 
                               Integer minimalPointFilter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<LabWork> root = cq.from(LabWork.class);
        cq.select(cb.count(root));
        
        List<Predicate> predicates = new ArrayList<>();
        
        if (nameFilter != null && !nameFilter.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")), 
                                 "%" + nameFilter.toLowerCase() + "%"));
        }
        
        if (difficultyFilter != null && !difficultyFilter.trim().isEmpty()) {
            predicates.add(cb.equal(root.get("difficulty"), difficultyFilter));
        }
        
        if (minimalPointFilter != null) {
            predicates.add(cb.equal(root.get("minimalPoint"), minimalPointFilter));
        }
        
        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }
        
        TypedQuery<Long> query = entityManager.createQuery(cq);
        return query.getSingleResult();
    }
    
    /**
     * Удалить все лабораторные работы с определенным минимальным количеством баллов
     */
    public int deleteByMinimalPoint(int minimalPoint) {
        EntityTransaction tr = entityManager.getTransaction();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LabWork> cq = cb.createQuery(LabWork.class);
        Root<LabWork> root = cq.from(LabWork.class);
        
        cq.where(cb.equal(root.get("minimalPoint"), minimalPoint));
        
        TypedQuery<LabWork> query = entityManager.createQuery(cq);
        List<LabWork> labWorks = query.getResultList();
        
        int count = 0;
        tr.begin();
        for (LabWork labWork : labWorks) {
            entityManager.remove(labWork);
            entityManager.flush();
            count++;
        }
        tr.commit();
        
        return count;
    }
    
    /**
     * Подсчитать количество лабораторных работ с определенным максимальным количеством баллов за личные качества
     */
    public long countByPersonalQualitiesMaximum(Float personalQualitiesMaximum) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<LabWork> root = cq.from(LabWork.class);
        
        cq.where(cb.equal(root.get("personalQualitiesMaximum"), personalQualitiesMaximum));
        cq.select(cb.count(root));
        
        TypedQuery<Long> query = entityManager.createQuery(cq);
        return query.getSingleResult();
    }
    
    /**
     * Подсчитать количество лабораторных работ с автором меньше заданного
     */
    public long countByAuthorLessThan(Long authorId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<LabWork> root = cq.from(LabWork.class);
        
        cq.where(cb.lessThan(root.get("author").get("id"), authorId));
        cq.select(cb.count(root));
        
        TypedQuery<Long> query = entityManager.createQuery(cq);
        return query.getSingleResult();
    }
}
