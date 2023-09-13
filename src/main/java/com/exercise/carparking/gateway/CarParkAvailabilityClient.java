package com.exercise.carparking.gateway;

import com.exercise.carparking.service.dto.CarparkData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "carpark-availability-api", url = "https://api.data.gov.sg/v1/transport/carpark-availability")
public interface CarParkAvailabilityClient {
    @GetMapping
    CarparkData fetchCarParkAvailability();
}
