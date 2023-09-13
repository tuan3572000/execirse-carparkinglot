package com.exercise.carparking.infra.taskimpl;

import com.exercise.carparking.domain.event.ChangedCarParkAvailabilityEvent;
import com.exercise.carparking.domain.model.CarParkAvailability;
import com.exercise.carparking.gateway.CarParkAvailabilityClient;
import com.exercise.carparking.repository.CarParkAvailabilityRepository;
import com.exercise.carparking.service.CarParkAvailabilityTask;
import com.exercise.carparking.service.dto.CarParkInfoDTO;
import com.exercise.carparking.service.dto.CarparkData;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class CarParkAvailabilityTaskImpl implements CarParkAvailabilityTask {
    private CarParkAvailabilityClient carParkAvailabilityClient;
    private CarParkAvailabilityRepository carParkAvailabilityRepository;
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void sync() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        CarparkData carparkData = carParkAvailabilityClient.fetchCarParkAvailability();
        Function<CarparkData.Carpark, List<CarParkAvailability>> toCarParkAvailabilitiesFunction = (carpark) ->
            carpark.getCarparkInfo().stream()
                .map((item) -> CarParkAvailability.builder()
                    .carParkNo(carpark.getCarparkNumber())
                    .updatedAt(LocalDateTime.parse(carpark.getUpdateDatetime(), formatter))
                    .availableLots(Integer.parseInt(item.getLotsAvailable()))
                    .totalLots(Integer.parseInt(item.getTotalLots()))
                    .lotType(item.getLotType())
                    .build()
                ).toList();
        

        List<CarParkAvailability> carParkAvailabilities = carparkData.getItems().stream()
            .map(CarparkData.Item::getCarparkData)
            .flatMap(List::stream)
            .map(toCarParkAvailabilitiesFunction)
            .flatMap(List::stream)
            .toList();

        int result = carParkAvailabilityRepository.save(carParkAvailabilities);
        applicationEventPublisher.publishEvent(new ChangedCarParkAvailabilityEvent(result));
    }
}
