package com.exercise.carparking.application.domain.repository;

import com.exercise.carparking.application.domain.service.dto.CarParkDTO;

import java.util.List;

public interface CarParkRepository {
    List<CarParkDTO> getAll();
}
