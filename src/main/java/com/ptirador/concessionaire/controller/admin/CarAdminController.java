package com.ptirador.concessionaire.controller.admin;

import com.ptirador.concessionaire.model.Car;
import com.ptirador.concessionaire.service.CarService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Home controller.
 *
 * @author ptirador
 */
@RestController
@RequestMapping("/administration/cars")
public class CarAdminController {

    /**
     * Cars JSON list URL.
     */
    private static final String URL_CARS_JSON_LIST = "/list/json";
    /**
     * Car deletion URL.
     */
    private static final String URL_DELETE_CAR = "/delete/{id:.+}";
    /**
     * Interface service for car management.
     */
    private final CarService carService;

    /**
     * Constructor.
     *
     * @param carService Interface service for car management.
     */
    public CarAdminController(final CarService carService) {
        this.carService = carService;
    }

    /**
     * Exports the full car list to JSON.
     *
     * @return Cars list in JSON format.
     */
    @GetMapping(value = URL_CARS_JSON_LIST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Car>> getCarsList() {
        List<Car> cars = carService.findAll();
        if (cars.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(cars);
    }

    /**
     * Deletes a car from its id.
     *
     * @param id car identificator.
     * @return operation response entity.
     */
    @DeleteMapping(value = URL_DELETE_CAR, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Car> deleteCar(@PathVariable String id) {
        Car car = carService.findById(id);
        if (car == null) {
            return ResponseEntity.notFound().build();
        }
        carService.deleteCarById(id);
        return ResponseEntity.noContent().build();
    }

}