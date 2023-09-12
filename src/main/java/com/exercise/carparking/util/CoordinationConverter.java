package com.exercise.carparking.util;

import org.locationtech.proj4j.*;
import org.springframework.data.geo.Point;

public class CoordinationConverter {
    private static CoordinationConverter instance;
    private  final CoordinateTransform SVY21_TRANSFORM;
    private CoordinationConverter() {
        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem SVY21 = crsFactory.createFromName("epsg:3414");
        CoordinateReferenceSystem WGS84 = crsFactory.createFromName("epsg:4326");
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        SVY21_TRANSFORM = ctFactory.createTransform(SVY21, WGS84);
    }

    public static synchronized CoordinationConverter  getInstance() {
        if(instance != null) {
            return instance;
        }

        synchronized (""){
            if(instance == null) {
                instance = new CoordinationConverter();
            }
        }
        return instance;
    }

    public Point fromSVY21ToWGS84(double xCoordinate, double yCoordinate) {
        ProjCoordinate result = new ProjCoordinate();
        SVY21_TRANSFORM.transform(new ProjCoordinate(xCoordinate, yCoordinate), result);
        return new Point(result.x, result.y);
    }
}
