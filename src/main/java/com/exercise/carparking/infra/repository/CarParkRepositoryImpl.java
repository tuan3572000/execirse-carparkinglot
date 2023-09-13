package com.exercise.carparking.infra.repository;

import com.exercise.carparking.application.domain.repository.CarParkRepository;
import com.exercise.carparking.application.domain.service.dto.CarParkDTO;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
class CarParkRepositoryImpl implements CarParkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<CarParkDTO> getAll() {
        String query = "SELECT address, longitude, latitude, available_lots, total_lots FROM CAR_PARK_AVAILABILITY avail " +
            "INNER JOIN CAR_PARK_LOCATION location ON avail.car_park_no = location.car_park_no WHERE avail.available_lots > 0;";
        return jdbcTemplate.query(query, (rs, rowNum) ->
            CarParkDTO.builder()
                .address(rs.getString("address"))
                .latitude(rs.getDouble("latitude"))
                .longitude(rs.getDouble("longitude"))
                .availableLots(rs.getInt("available_lots"))
                .totalLots(rs.getInt("total_lots"))
                .build()
        );
    }
}
