package com.ptirador.concessionaire.service;

import com.ptirador.concessionaire.model.Car;

import java.util.List;

/**
 * Interface service for car management.
 *
 * @author ptirador
 */
public interface CarService {

    /**
     * Obtains a full list of cars.
     *
     * @return list of all available cars.
     */
    List<Car> findAll();

    /**
     * Obtains a list of cars by matching the search text.
     *
     * @param search    search text.
     * @param sortField field to sort by.
     * @param order     sort order.
     * @return list of filtered cars.
     */
    List<Car> findByAnyField(String search, String sortField, String order);

    /**
     * Obtains a car by id.
     *
     * @param id car identificator.
     * @return car with the corresponding id.
     */
    Car findById(String id);

    /**
     * Saves a car object.
     *
     * @param car car object.
     * @return the saved object.
     */
    Car save(Car car);

    /**
     * Deletes a car by id.
     *
     * @param id car identificator.
     */
    void deleteCarById(String id);
}
