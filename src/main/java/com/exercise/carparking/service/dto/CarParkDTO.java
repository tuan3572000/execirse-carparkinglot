package com.exercise.carparking.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class CarParkDTO implements Serializable {
    String address;
    double latitude;
    double longitude;
    @JsonProperty("total_lots")
    int totalLots;
    @JsonProperty("available_lots")
    int availableLots;
}