package com.ptirador.concessionaire.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Bean that represents a car.
 *
 * @author ptirador
 */
@Document(collection = "cars")
@Getter
@Setter
public class Car {

    /**
     * Car identifier.
     */
    @Id
    private String id;

    /**
     * Make name.
     */
    @NotEmpty(message = "{error.mandatoryField}")
    private String make;

    /**
     * Model name.
     */
    @NotEmpty(message = "{error.mandatoryField}")
    private String model;

    /**
     * Model year.
     */
    @Min(value = 1860, message = "{error.year.greaterThan}")
    @NotNull(message = "{error.mandatoryField}")
    private Integer year;
}
