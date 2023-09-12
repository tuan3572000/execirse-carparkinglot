package com.exercise.carparking.gateway;

import com.exercise.carparking.service.dto.CarParkInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "carpark-availability-api", url = "https://api.data.gov.sg/v1/transport/carpark-availability")
public interface CarParkAvailabilityClient {
    @GetMapping
    List<CarParkInfoDTO> fetchCarParkAvailability();
}
