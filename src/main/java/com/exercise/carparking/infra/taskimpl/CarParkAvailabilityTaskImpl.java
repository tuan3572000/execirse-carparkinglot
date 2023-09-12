package com.exercise.carparking.infra.taskimpl;

import com.exercise.carparking.domain.event.ChangedCarParkAvailabilityEvent;
import com.exercise.carparking.domain.model.CarParkAvailability;
import com.exercise.carparking.gateway.CarParkAvailabilityClient;
import com.exercise.carparking.repository.CarParkAvailabilityRepository;
import com.exercise.carparking.service.CarParkAvailabilityTask;
import com.exercise.carparking.service.dto.CarParkInfoDTO;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
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
        List<CarParkInfoDTO> carParkInfoDTOs = carParkAvailabilityClient.fetchCarParkAvailability();
        Function<CarParkInfoDTO, List<CarParkAvailability>> toCarParkAvailabilitiesFunction = (carParkInfoDTO) ->
            Arrays.stream(carParkInfoDTO.getCarparkInfo())
                .map((item) -> CarParkAvailability.builder()
                    .carParkNo(carParkInfoDTO.getCarparkNumber())
                    .updatedAt(LocalDateTime.parse(carParkInfoDTO.getUpdateDatetime(), formatter))
                    .availableLots(Integer.parseInt(item.getLotsAvailable()))
                    .totalLots(Integer.parseInt(item.getTotalLots()))
                    .lotType(item.getLotType())
                    .build()
                ).toList();

        List<CarParkAvailability> carParkAvailabilities = carParkInfoDTOs.stream()
            .map(toCarParkAvailabilitiesFunction)
            .flatMap(List::stream)
            .toList();

        int result = carParkAvailabilityRepository.save(carParkAvailabilities);
        applicationEventPublisher.publishEvent(new ChangedCarParkAvailabilityEvent(result));
    }
}
