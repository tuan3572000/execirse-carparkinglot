package com.exercise.carparking.infra.repositoryimpl;

import com.exercise.carparking.domain.model.CarParkLocation;
import com.exercise.carparking.repository.CarParkLocationRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
@AllArgsConstructor
class CarParkLocationRepositoryImpl implements CarParkLocationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int save(List<CarParkLocation> carParkLocations) {
        return jdbcTemplate.batchUpdate("INSERT INTO CAR_PARK_LOCATION VALUES (?, ?, ?, ?)",
            new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setString(1, carParkLocations.get(i).getCarParkNo());
                    ps.setString(2, carParkLocations.get(i).getAddress());
                    ps.setDouble(3, carParkLocations.get(i).getLongitude());
                    ps.setDouble(4, carParkLocations.get(i).getLatitude());
                }
                @Override
                public int getBatchSize() {
                    return carParkLocations.size();
                }
            }).length;
    }

    @Override
    public boolean hasData() {
        return jdbcTemplate.queryForObject("count(1) from CAR_PARK_LOCATION", Integer.class) > 0;
    }
}
