package com.exercise.carparking.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"carParkNo", "lotType"})
public class CarParkAvailability {
    private String carParkNo;
    private String lotType;
    private int totalLots;
    private int availableLots;
    // Singapore time
    LocalDateTime updatedAt;
}