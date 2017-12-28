package com.ptirador.concessionaire.repository;

import com.ptirador.concessionaire.model.Car;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends MongoRepository<Car, String> {

    List<Car> findByMakeLikeOrModelLikeAllIgnoreCase(String make, String model, Sort sort);

}
