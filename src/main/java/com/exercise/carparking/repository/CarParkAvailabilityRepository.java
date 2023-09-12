package com.exercise.carparking.repository;

import com.exercise.carparking.domain.model.CarParkAvailability;

import java.util.List;

public interface CarParkAvailabilityRepository {
    int save(List<CarParkAvailability> carParkLocations);
}
