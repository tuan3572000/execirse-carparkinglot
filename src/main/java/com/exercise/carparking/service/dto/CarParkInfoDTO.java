package com.exercise.carparking.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CarParkInfoDTO {
    @JsonProperty("carpark_number")
    private String carparkNumber;

    @JsonProperty("update_datetime")
    private String updateDatetime;

    @JsonProperty("carpark_info")
    private CarparkInfo[] carparkInfo;

    @Data
    public static class CarparkInfo {
        @JsonProperty("total_lots")
        private String totalLots;

        @JsonProperty("lot_type")
        private String lotType;

        @JsonProperty("lots_available")
        private String lotsAvailable;
    }
}
