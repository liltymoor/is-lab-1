package com.crudoshlep.islab1.service.impl;

import com.crudoshlep.islab1.dao.CoordinatesDAO;
import com.crudoshlep.islab1.model.Coordinates;
import com.crudoshlep.islab1.service.CoordinatesService;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

public class CoordinatesServiceImpl implements CoordinatesService {
    @Inject
    private CoordinatesDAO coordinatesDAO;

    @Override
    public Coordinates createCoordinates(Coordinates coords) {
        return coordinatesDAO.save(coords);
    }

    @Override
    public Optional<Coordinates> getCoordinatesById(Long id) {
        return coordinatesDAO.findById(id);
    }

    @Override
    public Coordinates updateCoordinates(Coordinates coords) {
        return coordinatesDAO.update(coords);
    }

    @Override
    public boolean deleteCoordinatesById(Long id) {
        return coordinatesDAO.deleteById(id);
    }

    @Override
    public List<Coordinates> getAllCoordinates() {
        return coordinatesDAO.findAll();
    }

    @Override
    public List<Coordinates> getCoordinatesWithFilters(Integer xFilter, Float yFilter, int offset, int limit) {
        return coordinatesDAO.findWithFilters(xFilter, yFilter, offset, limit);
    }

    @Override
    public long getTotalCount() {
        return 0;
    }
}
