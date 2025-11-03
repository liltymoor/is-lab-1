package com.crudoshlep.islab1.dao;

import com.crudoshlep.islab1.model.Coordinates;
import com.crudoshlep.islab1.model.Location;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class CoordinatesDAO extends BaseDAO<Coordinates> {
    public CoordinatesDAO() {
        super(Coordinates.class);
    }

    /**
     * Найти коордианты с фильтрацией
     */
    public List<Coordinates> findWithFilters(Integer x, Float y, int offset, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Coordinates> cq = cb.createQuery(Coordinates.class);
        Root<Coordinates> root = cq.from(Coordinates.class);

        List<Predicate> predicates = new ArrayList<>();

        if (x != null) {
            predicates.add(cb.equal(root.get("x"), x));
        }

        if (y != null) {
            predicates.add(cb.between(root.get("y"), y - 1, y + 1));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.asc(root.get("id")));

        TypedQuery<Coordinates> query = entityManager.createQuery(cq);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }
}
