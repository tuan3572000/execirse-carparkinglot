package com.exercise.carparking.infra.repositoryimpl;

import com.exercise.carparking.repository.CarParkRepository;
import com.exercise.carparking.service.dto.CarParkDTO;
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
            "LEFT JOIN CAR_PARK_LOCATION location ON avail.car_park_no = location.car_park_no WHERE avail.available_lots > 0;";
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
