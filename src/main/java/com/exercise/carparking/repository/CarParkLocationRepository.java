package com.exercise.carparking.repository;

import com.exercise.carparking.domain.model.CarParkLocation;

import java.util.List;

public interface CarParkLocationRepository {
    int save(List<CarParkLocation> carParkLocations);

    boolean isEmpty();
}
