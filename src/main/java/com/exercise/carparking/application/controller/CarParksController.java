package com.exercise.carparking.application.controller;

import com.exercise.carparking.application.domain.service.CarParkAvailabilitySyncTask;
import com.exercise.carparking.application.domain.service.CarParkService;
import com.exercise.carparking.application.domain.service.dto.CarParkDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("carparks")
@AllArgsConstructor
public class CarParksController {

    private CarParkService carParkService;
    private CarParkAvailabilitySyncTask carParkAvailabilitySyncTask;
    @GetMapping("nearest")
    public List<CarParkDTO> nearestAvailableCarParks(@RequestParam("longitude") double longitude,
                                                     @RequestParam("latitude") double latitude,
                                                     @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                                                     @RequestParam(value = "per_page", defaultValue = "30", required = false) int per_page) {
        return carParkService.nearest(longitude, latitude, page, per_page);
    }

    @GetMapping("availability-sync")
    public void refreshCarParkAvailability() {
        carParkAvailabilitySyncTask.sync();
    }

}