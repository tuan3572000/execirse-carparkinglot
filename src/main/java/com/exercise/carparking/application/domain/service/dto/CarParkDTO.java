package com.exercise.carparking.application.domain.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class CarParkDTO implements Serializable {
    @JsonProperty("address")
    String address;
    @JsonProperty("latitude")
    double latitude;
    @JsonProperty("longitude")
    double longitude;
    @JsonProperty("total_lots")
    int totalLots;
    @JsonProperty("available_lots")
    int availableLots;
}
