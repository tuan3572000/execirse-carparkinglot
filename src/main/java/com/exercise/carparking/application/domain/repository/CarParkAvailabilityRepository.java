package com.exercise.carparking.application.domain.repository;

import com.exercise.carparking.application.domain.model.CarParkAvailability;

import java.util.List;

public interface CarParkAvailabilityRepository {
    int save(List<CarParkAvailability> carParkLocations);
}
