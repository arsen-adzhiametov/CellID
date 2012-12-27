package com.example;

import java.util.HashMap;

public class MetroStations {

    private static HashMap<Integer, String> metroStations;

    static {

        metroStations = new HashMap<Integer, String>();

        metroStations.put(29931,"Academgorodok");
        metroStations.put(29930, "Zhitomirskaya");
        metroStations.put(29929,"Svyatoshin");
        metroStations.put(29928,"Nyvky");
        metroStations.put(29927,"Beresteyskaya");
        metroStations.put(29926,"Shulyavskaya");
        metroStations.put(29925,"Polytechnicheskiy Universitet");
        metroStations.put(29924,"Vokzalnaya");
        metroStations.put(29923,"Universitet");
        metroStations.put(29922,"Teatralnaya");
        metroStations.put(29921,"Khreshatyk");
        metroStations.put(29920,"Arsenalnaya");

    }

    public static HashMap<Integer, String> getMetroStations() {
        return metroStations;
    }
}
