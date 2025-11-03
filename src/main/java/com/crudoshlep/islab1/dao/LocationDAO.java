package com.crudoshlep.islab1.dao;

import com.crudoshlep.islab1.model.Location;
import com.crudoshlep.islab1.model.Person;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class LocationDAO extends BaseDAO<Location>{
    public LocationDAO() {
        super(Location.class);
    }

    /**
     * Найти локации с фильтрацией
     */
    public List<Location> findWithFilters(String nameFilter, Double x, Float y, Float z, int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> cq = cb.createQuery(Location.class);
        Root<Location> root = cq.from(Location.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nameFilter != null && !nameFilter.trim().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("name")),
                    "%" + nameFilter.toLowerCase() + "%"));
        }

        if (x != null) {
            predicates.add(cb.between(root.get("x"), x - 1, x + 1));
        }

        if (y != null) {
            predicates.add(cb.between(root.get("y"), y - 1, y + 1));
        }

        if (z != null) {
            predicates.add(cb.between(root.get("z"), z - 1, z + 1));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.asc(root.get("id")));

        TypedQuery<Location> query = entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }
}
