package com.exercise.carparking.infra.taskimpl;

import com.exercise.carparking.domain.event.ChangedCarParkAvailabilityEvent;
import com.exercise.carparking.service.CarParkAvailableIndexingService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class ChangedCarParkAvailabilityEventListener implements ApplicationListener<ChangedCarParkAvailabilityEvent> {
    private CarParkAvailableIndexingService carParkAvailableIndexingService;

    @Override
    public void onApplicationEvent(@NonNull ChangedCarParkAvailabilityEvent event) {
        log.info("Car park availability changed, rebuild cache");
        carParkAvailableIndexingService.indexAvailableCarParks();
    }
}
