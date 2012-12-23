package com.example;

import java.util.HashMap;

public class MetroStations {

    private static HashMap<String, Integer> metroStations;

    public static synchronized HashMap getStations() {
        if (metroStations == null) {

            metroStations = new HashMap<String, Integer>();
            metroStations.put("Academgorodok", 29931);
            metroStations.put("Zhitomirskaya", 29930);
            metroStations.put("Svyatoshin", 29929);
            metroStations.put("Nyvky", 29928);
            metroStations.put("Beresteyskaya", 29927);
            metroStations.put("Shulyavskaya", 29926);
            metroStations.put("Polytechnicheskiy Universitet", 29925);
            metroStations.put("Vokzalnaya", 29924);
            metroStations.put("Universitet", 29923);
            metroStations.put("Teatralnaya", 29922);
            metroStations.put("Khreshatyk", 29921);
            metroStations.put("Arsenalnaya", 29920);
        }
        return metroStations;
    }

}
