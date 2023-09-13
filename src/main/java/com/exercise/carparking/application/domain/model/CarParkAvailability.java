package com.exercise.carparking.application.domain.model;

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
    // Singapore time
    LocalDateTime updatedAt;
    private String carParkNo;
    private String lotType;
    private int totalLots;
    private int availableLots;
}
