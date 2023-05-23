package com.knotted.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

// 기하학 관련 유틸 모음
public class GeometryUtils {
    
    // 거리 계산 공식을 적용해 두 좌표 간의 거리를 미터 단위로 반환함
    public static Long calculateDistance(Point p1, Point p2){
        GeometryFactory geometryFactory = new GeometryFactory();
        
        // 위경도 좌표 가져오기
        Coordinate c1 = p1.getCoordinate();
        Coordinate c2 = p2.getCoordinate();

        // 허베르스인 공식을 사용하여 거리 계산
        double lat1 = Math.toRadians(c1.getY());
        double lon1 = Math.toRadians(c1.getX());
        double lat2 = Math.toRadians(c2.getY());
        double lon2 = Math.toRadians(c2.getX());

        double earthRadius = 6371000; // 지구의 반지름 (미터 단위)
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadius * c;

        return Math.round(distance); // 반올림하여 반환
    }
}
