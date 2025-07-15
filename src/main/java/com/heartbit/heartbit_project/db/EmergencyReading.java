package com.heartbit.heartbit_project.db;

import java.sql.Timestamp;

public class EmergencyReading {
    private final int bpmValue;
    private final String lat;
    private final String lon;
    private final Timestamp dateTime;
    private final int has_contacted;

    public EmergencyReading(int bpmValue, String lat, String lon, Timestamp dateTime, int has_contacted) {
        this.bpmValue = bpmValue;
        this.lat = lat;
        this.lon = lon;
        this.dateTime = dateTime;
        this.has_contacted = has_contacted;
    }

    public int getBpmValue() {
        return bpmValue;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
    public Timestamp getDateTime() {
        return dateTime;
    }
    public int getHas_contacted() {
        return has_contacted;
    }
}
