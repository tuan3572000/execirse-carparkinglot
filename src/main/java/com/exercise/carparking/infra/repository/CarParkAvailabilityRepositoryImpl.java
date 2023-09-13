package com.exercise.carparking.infra.repository;

import com.exercise.carparking.application.domain.model.CarParkAvailability;
import com.exercise.carparking.application.domain.repository.CarParkAvailabilityRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@AllArgsConstructor
class CarParkAvailabilityRepositoryImpl implements CarParkAvailabilityRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public int save(List<CarParkAvailability> carParkLocations) {
        String sql = "INSERT INTO car_park_availability (car_park_no, lot_type, available_lots, total_lots, updated_at) " +
            "VALUES (:car_park_no, :lot_type, :available_lots, :total_lots, :updated_at) " +
            "ON CONFLICT (car_park_no, lot_type) DO UPDATE SET " +
            "available_lots=:available_lots, " +
            "total_lots=:total_lots, " +
            "updated_at=:updated_at";

        MapSqlParameterSource[] batchParams = carParkLocations.stream()
            .map(item -> new MapSqlParameterSource()
                .addValue("car_park_no", item.getCarParkNo())
                .addValue("lot_type", item.getLotType())
                .addValue("available_lots", item.getAvailableLots())
                .addValue("total_lots", item.getTotalLots())
                .addValue("updated_at", Timestamp.valueOf(item.getUpdatedAt())))
            .toArray(MapSqlParameterSource[]::new);

        return jdbcTemplate.batchUpdate(sql, batchParams).length;
    }

}
