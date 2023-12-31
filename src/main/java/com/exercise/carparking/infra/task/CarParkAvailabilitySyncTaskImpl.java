package com.exercise.carparking.infra.task;

import com.exercise.carparking.application.domain.service.CarParkAvailabilityService;
import com.exercise.carparking.application.domain.service.CarParkAvailabilitySyncTask;
import com.exercise.carparking.application.domain.service.CarParkAvailableIndexingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class CarParkAvailabilitySyncTaskImpl implements CarParkAvailabilitySyncTask {
    private CarParkAvailableIndexingService carParkAvailableIndexingService;
    private CarParkAvailabilityService carParkAvailabilityService;


    @Override
    public void execute() {
        carParkAvailabilityService.syncAvailableCarParks();
        carParkAvailableIndexingService.indexAvailableCarParks();
    }
}
