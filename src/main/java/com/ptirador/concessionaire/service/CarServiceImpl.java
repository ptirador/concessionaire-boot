package com.ptirador.concessionaire.service;

import com.ptirador.concessionaire.model.Car;
import com.ptirador.concessionaire.repository.CarRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation service for car management.
 *
 * @author ptirador
 */
@Service
public class CarServiceImpl implements CarService {

    /**
     * Repository for cars.
     */
    private CarRepository carRepository;

    /**
     * Constructor.
     *
     * @param carRepository repository for cars.
     */
    public CarServiceImpl(final CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> findAll() {
        Sort sort = getDefaultSort();
        return carRepository.findAll(sort);
    }

    private Sort getDefaultSort() {
        return new Sort(Sort.Direction.ASC, "make", "model", "year");
    }

    @Override
    public List<Car> findByAnyField(String search, String sortField, String order) {
        Sort sort;
        if ("undefined".equals(sortField)) {
            sort = getDefaultSort();
        } else {
            sort = new Sort(Sort.Direction.fromString(order), sortField);
        }

        return carRepository.findByMakeLikeOrModelLikeAllIgnoreCase(search, search, sort);
    }

    @Override
    public Car findById(String id) {
        return carRepository.findOne(id);
    }

    @Override
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Override
    public void deleteCarById(String id) {
        carRepository.delete(id);
    }
}
