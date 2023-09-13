package com.exercise.carparking.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
class CarParkApiModel {
    @JsonProperty("items")
    private List<Item> items;

    @Data
    public static class Item {
        @JsonProperty("timestamp")
        private String timestamp;
        @JsonProperty("carpark_data")
        private List<Carpark> carparkData;
    }

    @Data
    public static class Carpark {
        @JsonProperty("carpark_info")
        private List<CarparkInfo> carparkInfo;
        @JsonProperty("carpark_number")
        private String carparkNumber;
        @JsonProperty("update_datetime")
        private String updateDatetime;
    }

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
