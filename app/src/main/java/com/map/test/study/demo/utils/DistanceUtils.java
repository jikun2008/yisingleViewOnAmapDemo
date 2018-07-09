package com.map.test.study.demo.utils;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;

import java.util.List;

/**
 * @author jikun
 * Created by jikun on 2018/7/9.
 */
public class DistanceUtils {


    public static float calculateTwoPointDistance(LatLng latLng1, LatLng latLng2) {
        return AMapUtils.calculateLineDistance(latLng1, latLng2);
    }

    public static float calculateTwoPointDistance(LatLonPoint point1, LatLonPoint point2) {
        return AMapUtils.calculateLineDistance(new LatLng(point1.getLatitude(), point1.getLongitude())
                , new LatLng(point2.getLatitude(), point2.getLongitude()));
    }


    public static float calcaulateListDistance(List<LatLng> list) {
        LatLng previousLatLng = null;
        float distance = 0f;
        for (LatLng latLng : list) {
            if (null == previousLatLng) {
                break;
            }
            distance = distance + calculateTwoPointDistance(previousLatLng, latLng);

            previousLatLng = latLng;
        }

        return distance;

    }
}
