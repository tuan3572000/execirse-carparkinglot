package com.exercise.carparking.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "carParkNo")
public class CarParkLocation {
    private String carParkNo;
    private String address;
    private double latitude;
    private double longitude;
}
