package com.heartbit.heartbit_project.network;


public class DataPoint {
    private final int bpm;
    private final long millis;

    public DataPoint(int bpm, long millis) {
        this.bpm = bpm;
        this.millis = millis;
    }

    public int bpm() {
        return bpm;
    }

    public long millis() {
        return millis;
    }
}
