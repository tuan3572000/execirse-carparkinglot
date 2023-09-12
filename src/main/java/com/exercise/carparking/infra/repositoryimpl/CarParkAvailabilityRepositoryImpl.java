package com.exercise.carparking.infra.repositoryimpl;

import com.exercise.carparking.domain.model.CarParkAvailability;
import com.exercise.carparking.repository.CarParkAvailabilityRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@AllArgsConstructor
class CarParkAvailabilityRepositoryImpl implements CarParkAvailabilityRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int save(List<CarParkAvailability> carParkLocations) {
        try{
            return jdbcTemplate.batchUpdate("UPDATE CAR_PARK_AVAILABILITY SET available_lots = ?, total_lots = ?, updated_at = ? WHERE car_park_no = ? AND lot_type = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, carParkLocations.get(i).getAvailableLots());
                        ps.setInt(2, carParkLocations.get(i).getTotalLots());
                        ps.setTimestamp(3, Timestamp.valueOf(carParkLocations.get(i).getUpdatedAt()));
                        ps.setString(4, carParkLocations.get(i).getCarParkNo());
                        ps.setString(5, carParkLocations.get(i).getLotType());
                    }
                    @Override
                    public int getBatchSize() {
                        return carParkLocations.size();
                    }
                }).length;
        } catch (Exception e) {
            return jdbcTemplate.batchUpdate("INSERT INTO CAR_PARK_AVAILABILITY VALUES (?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, carParkLocations.get(i).getCarParkNo());
                        ps.setInt(2, carParkLocations.get(i).getAvailableLots());
                        ps.setInt(3, carParkLocations.get(i).getTotalLots());
                        ps.setString(4, carParkLocations.get(i).getLotType());
                        ps.setTimestamp(5, Timestamp.valueOf(carParkLocations.get(i).getUpdatedAt()));
                    }
                    @Override
                    public int getBatchSize() {
                        return carParkLocations.size();
                    }
                }).length;
        }

    }

}
