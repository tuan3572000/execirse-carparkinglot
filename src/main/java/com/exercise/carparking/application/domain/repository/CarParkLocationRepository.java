package com.exercise.carparking.application.domain.repository;

import com.exercise.carparking.application.domain.model.CarParkLocation;

import java.util.List;

public interface CarParkLocationRepository {
    int save(List<CarParkLocation> carParkLocations);

    boolean isEmpty();
}
