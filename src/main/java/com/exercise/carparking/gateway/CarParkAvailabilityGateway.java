package com.exercise.carparking.gateway;

import com.exercise.carparking.application.domain.model.CarParkAvailability;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class CarParkAvailabilityGateway {
    private CarParkAvailabilityApi carParkAvailabilityApi;

    public List<CarParkAvailability> crawlAvailableCarParks() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        CarParkApiModel carparkApiModel = carParkAvailabilityApi.fetchCarParkAvailability();
        Function<CarParkApiModel.Carpark, List<CarParkAvailability>> toCarParkAvailabilitiesFunction = (carpark) ->
            carpark.getCarparkInfo().stream()
                .map((item) -> CarParkAvailability.builder()
                    .carParkNo(carpark.getCarparkNumber())
                    .updatedAt(LocalDateTime.parse(carpark.getUpdateDatetime(), formatter))
                    .availableLots(Integer.parseInt(item.getLotsAvailable()))
                    .totalLots(Integer.parseInt(item.getTotalLots()))
                    .lotType(item.getLotType())
                    .build()
                ).toList();


        return carparkApiModel.getItems().stream()
            .map(CarParkApiModel.Item::getCarparkData)
            .flatMap(List::stream)
            .map(toCarParkAvailabilitiesFunction)
            .flatMap(List::stream)
            .toList();
    }
}
