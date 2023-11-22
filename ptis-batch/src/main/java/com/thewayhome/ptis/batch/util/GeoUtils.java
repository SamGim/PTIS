package com.thewayhome.ptis.batch.util;

public class GeoUtils {
    public static final double RADIUS_OF_EARTH_KM = 6371.01; // 지구의 반지름 (킬로미터)

    public static long calculateDistance(double lat1, double lon1, double lat2, double lon2, boolean isEuclidean) {
        // 두 좌표의 차이 계산
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Haversine 공식 적용
        double dlon = lon2Rad - lon1Rad;
        double dlat = lat2Rad - lat1Rad;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 결과 반환 (미터 단위)
        double distance = RADIUS_OF_EARTH_KM * c;
        double euc_distance = Math.sqrt(distance / 2.0) * 2.0;
        return (long) ((isEuclidean ? euc_distance : distance) * 1000L);
    }

    /*
    * @param distance 거리 (미터)
    * @param speed 속도 (km/h)
    * @return 소요 시간 (분)
     */
    public static long calculateTime(long distance_m, long speed_km_h) {
        double distance_km = distance_m / 1000.0;
        double speed_km_m = speed_km_h / 60.0;
        return Math.round(distance_km / speed_km_m);
    }
}
