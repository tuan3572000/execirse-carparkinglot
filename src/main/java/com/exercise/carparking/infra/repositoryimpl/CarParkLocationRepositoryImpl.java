package com.exercise.carparking.infra.repositoryimpl;

import com.exercise.carparking.domain.model.CarParkLocation;
import com.exercise.carparking.repository.CarParkLocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Repository
@AllArgsConstructor
class CarParkLocationRepositoryImpl implements CarParkLocationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public int save(List<CarParkLocation> carParkLocations) {
        String sql = "INSERT INTO car_park_location (car_park_no, address, latitude, longitude) " +
            "VALUES (:car_park_no, :address, :longitude, :latitude);";

        MapSqlParameterSource[] batchParams = carParkLocations.stream()
            .map(item -> new MapSqlParameterSource()
                .addValue("car_park_no", item.getCarParkNo())
                .addValue("address", item.getAddress())
                .addValue("longitude", item.getLongitude())
                .addValue("latitude", item.getLatitude()))
            .toArray(MapSqlParameterSource[]::new);

        return jdbcTemplate.batchUpdate(sql, batchParams).length;
    }

    @Override
    public boolean isEmpty() {
        return jdbcTemplate.queryForObject("select count(1) from CAR_PARK_LOCATION", new HashMap<>(), Integer.class) == 0;
    }
}
