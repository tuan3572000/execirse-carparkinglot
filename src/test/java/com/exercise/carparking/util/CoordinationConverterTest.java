package com.exercise.carparking.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Point;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CoordinationConverterTest {

    @Test
    public void shouldConvertFrom_SVY21_To_WGS84 (){
        double xCoord = 30314.7936;
        double yCoord = 31490.4942;
        Point point = CoordinationConverter.getInstance().fromSVY21ToWGS84(xCoord, yCoord);
        assertThat(point.getX(), is(103.85411804993093));
        assertThat(point.getY(), is(1.3010632720874935));
    }
}
