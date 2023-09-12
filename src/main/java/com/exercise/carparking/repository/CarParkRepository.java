package com.exercise.carparking.repository;

import com.exercise.carparking.service.dto.CarParkDTO;

import java.util.List;

public interface CarParkRepository {
    List<CarParkDTO> getAll();
}
