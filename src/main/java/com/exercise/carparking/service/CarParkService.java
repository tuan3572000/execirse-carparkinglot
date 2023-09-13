package com.exercise.carparking.service;

import com.exercise.carparking.domain.model.CarParkLocation;
import com.exercise.carparking.repository.CarParkLocationRepository;
import com.exercise.carparking.repository.CarParkRepository;
import com.exercise.carparking.service.dto.CarParkDTO;
import com.exercise.carparking.util.CSVFileReader;
import com.exercise.carparking.util.CoordinationConverter;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoLocation;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
public class CarParkService implements CarParkAvailableIndexingService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final CarParkLocationRepository carParkLocationRepository;
    private CarParkRepository carParkRepository;
    private static final String CAR_PARK_REDIS_KEY = "carpark:available";

    @PostConstruct
    private void init() {
        if (this.carParkLocationRepository.isEmpty()) {
            CoordinationConverter coordinationConverter = CoordinationConverter.getInstance();
            Iterator<String[]> rawCarParkLocationsIterator = CSVFileReader.read("classpath:HDBCarparkInformation.csv").iterator();
            List<CarParkLocation> batchCarParkLocations = new ArrayList<>();
            while (rawCarParkLocationsIterator.hasNext()) {
                String[] row = rawCarParkLocationsIterator.next();

                CarParkLocation carParkLocation = new CarParkLocation();
                carParkLocation.setCarParkNo(row[0]);
                carParkLocation.setAddress(row[1]);
                Point point = coordinationConverter.fromSVY21ToWGS84(Double.parseDouble(row[2]), Double.parseDouble(row[3]));
                carParkLocation.setLongitude(point.getY());
                carParkLocation.setLatitude(point.getX());
                batchCarParkLocations.add(carParkLocation);

                if (batchCarParkLocations.size() == 100) {
                    carParkLocationRepository.save(batchCarParkLocations);
                    batchCarParkLocations.clear();
                }
            }
            if (!batchCarParkLocations.isEmpty()) {
                carParkLocationRepository.save(batchCarParkLocations);
            }
        }
    }

    public List<CarParkDTO> nearest(double longitude, double latitude, int page, int perPage) {
        GeoResults result = redisTemplate.opsForGeo().radius(CAR_PARK_REDIS_KEY, new Circle(new Point(longitude, latitude), Double.MAX_VALUE));
        List<GeoResult<GeoLocation>> contents = result.getContent();
        int total = contents.size();
        int pages = (int) Math.ceil((double) total / perPage);
        if (page > pages) {
            return Collections.emptyList();
        }
        int fromIndex = (page - 1) * perPage;
        int toIndex = Math.min(total, fromIndex + perPage);
        return contents.subList(fromIndex, toIndex).stream().map((item) -> (CarParkDTO) item.getContent().getName()).toList();
    }

    public void indexAvailableCarParks() {
        Iterator<CarParkDTO> carParkDTOIterator = carParkRepository.getAll().iterator();
        final List<CarParkDTO> batchIndexingCarParkDTOs = new ArrayList<>();
        while (carParkDTOIterator.hasNext()) {
            batchIndexingCarParkDTOs.add(carParkDTOIterator.next());
            if (batchIndexingCarParkDTOs.size() == 1000) {
                indexToRedis(batchIndexingCarParkDTOs);
                batchIndexingCarParkDTOs.clear();
            }
        }
        if (!batchIndexingCarParkDTOs.isEmpty()) {
            indexToRedis(batchIndexingCarParkDTOs);
        }
    }

    private void indexToRedis(List<CarParkDTO> batchIndexingCarParkDTOs) {
        final byte[] key = CAR_PARK_REDIS_KEY.getBytes(StandardCharsets.UTF_8);
        redisTemplate.executePipelined(
            (RedisCallback<Integer>) connection -> {
                batchIndexingCarParkDTOs.forEach(item -> {
                    connection.geoCommands().geoRemove(key, SerializationUtils.serialize((item)));
                    connection.geoCommands().geoAdd(key, new Point(item.getLongitude(), item.getLatitude()), SerializationUtils.serialize((item)));
                }

                );
                return null;
            });
    }


}
