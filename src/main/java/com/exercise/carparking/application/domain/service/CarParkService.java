package com.exercise.carparking.application.domain.service;

import com.exercise.carparking.application.domain.model.CarParkAvailability;
import com.exercise.carparking.application.domain.model.CarParkLocation;
import com.exercise.carparking.application.domain.repository.CarParkAvailabilityRepository;
import com.exercise.carparking.application.domain.repository.CarParkLocationRepository;
import com.exercise.carparking.application.domain.repository.CarParkRepository;
import com.exercise.carparking.application.domain.service.dto.CarParkDTO;
import com.exercise.carparking.application.domain.service.dto.CarparkData;
import com.exercise.carparking.application.util.CSVFileReader;
import com.exercise.carparking.application.util.CoordinationConverter;
import com.exercise.carparking.gateway.CarParkAvailabilityClient;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class CarParkService implements CarParkAvailableIndexingService, CarParkAvailabilityService {
    private final RedisTemplate<String, CarParkDTO> redisTemplate;
    private final CarParkLocationRepository carParkLocationRepository;
    private CarParkRepository carParkRepository;
    private CarParkAvailabilityClient carParkAvailabilityClient;
    private CarParkAvailabilityRepository carParkAvailabilityRepository;
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
        this.syncAvailableCarParks();
        this.indexAvailableCarParks();
    }

    public List<CarParkDTO> nearest(double longitude, double latitude, int page, int perPage) {
        int fromElement = (page - 1) * perPage;
        int toElement = fromElement + perPage;
        RedisGeoCommands.GeoRadiusCommandArgs sortAscending = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().sortAscending().limit(toElement);
        GeoResults<RedisGeoCommands.GeoLocation<CarParkDTO>> result = redisTemplate.opsForGeo().radius(CAR_PARK_REDIS_KEY, new Circle(new Point(longitude, latitude), Double.MAX_VALUE), sortAscending);
        List<GeoResult<RedisGeoCommands.GeoLocation<CarParkDTO>>> contents = result.getContent();
        if (contents.size() < fromElement) {
            return Collections.emptyList();
        }
        return contents.subList(fromElement, Math.min(contents.size(), toElement)).stream().map((item) -> item.getContent().getName()).toList();
    }

    @Override
    public int syncAvailableCarParks() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        CarparkData carparkData = carParkAvailabilityClient.fetchCarParkAvailability();
        Function<CarparkData.Carpark, List<CarParkAvailability>> toCarParkAvailabilitiesFunction = (carpark) ->
            carpark.getCarparkInfo().stream()
                .map((item) -> CarParkAvailability.builder()
                    .carParkNo(carpark.getCarparkNumber())
                    .updatedAt(LocalDateTime.parse(carpark.getUpdateDatetime(), formatter))
                    .availableLots(Integer.parseInt(item.getLotsAvailable()))
                    .totalLots(Integer.parseInt(item.getTotalLots()))
                    .lotType(item.getLotType())
                    .build()
                ).toList();


        List<CarParkAvailability> carParkAvailabilities = carparkData.getItems().stream()
            .map(CarparkData.Item::getCarparkData)
            .flatMap(List::stream)
            .map(toCarParkAvailabilitiesFunction)
            .flatMap(List::stream)
            .toList();

        return carParkAvailabilityRepository.save(carParkAvailabilities);
    }

    @Override
    public void indexAvailableCarParks() {
        String outdatedKey = CAR_PARK_REDIS_KEY + "_outdated";
        redisTemplate.rename(CAR_PARK_REDIS_KEY, outdatedKey);
        redisTemplate.expireAt(outdatedKey, Instant.now());
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
                batchIndexingCarParkDTOs
                    .forEach(item -> connection.geoCommands().geoAdd(key, new Point(item.getLongitude(), item.getLatitude()), SerializationUtils.serialize((item))));
                return null;
            });
    }


}
