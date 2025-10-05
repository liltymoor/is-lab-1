package com.crudoshlep.islab1.dao;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Базовый DAO класс с общими операциями CRUD
 */
public abstract class BaseDAO<T> {
    
    @Inject
    protected EntityManager entityManager;
    
    protected final Class<T> entityClass;
    
    protected BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    /**
     * Сохранить сущность
     */
    public T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }
    
    /**
     * Обновить сущность
     */
    public T update(T entity) {
        return entityManager.merge(entity);
    }
    
    /**
     * Найти сущность по ID
     */
    public Optional<T> findById(Object id) {
        T entity = entityManager.find(entityClass, id);
        return Optional.ofNullable(entity);
    }
    
    /**
     * Найти все сущности
     */
    public List<T> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        TypedQuery<T> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
    
    /**
     * Удалить сущность по ID
     */
    public boolean deleteById(Object id) {
        T entity = entityManager.find(entityClass, id);
        if (entity != null) {
            entityManager.remove(entity);
            return true;
        }
        return false;
    }
    
    /**
     * Удалить сущность
     */
    public void delete(T entity) {
        entityManager.remove(entity);
    }
    
    /**
     * Подсчитать количество сущностей
     */
    public long count() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<T> root = cq.from(entityClass);
        cq.select(cb.count(root));
        TypedQuery<Long> query = entityManager.createQuery(cq);
        return query.getSingleResult();
    }
}
