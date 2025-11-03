package com.crudoshlep.islab1.service.impl;

import com.crudoshlep.islab1.dao.LocationDAO;
import com.crudoshlep.islab1.model.Location;
import com.crudoshlep.islab1.model.Person;
import com.crudoshlep.islab1.service.LocationService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;
import java.util.Optional;

@Stateless
@Named("locationService")
public class LocationServiceImpl implements LocationService {
    @Inject
    private LocationDAO locationDAO;

    @Override
    public Location createLocation(Location location) {
        return locationDAO.save(location);
    }

    @Override
    public Optional<Location> getLocationById(Long id) {
        return locationDAO.findById(id);
    }

    @Override
    public Location updateLocation(Location location) {
        return locationDAO.save(location);
    }

    @Override
    public boolean deleteLocationById(Long id) {
        return locationDAO.deleteById(id);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationDAO.findAll();
    }

    @Override
    public List<Location> getLocationsWithFilters(String nameFilter, Double xFilter, Float yFilter, Float zFilter, int offset, int limit) {
        return locationDAO.findWithFilters(nameFilter, xFilter, yFilter, zFilter, offset, limit);
    }

    @Override
    public long getTotalCount() {
        return locationDAO.count();
    }
}
