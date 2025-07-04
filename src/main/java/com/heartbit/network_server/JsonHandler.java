package com.heartbit.network_server;

import org.json.JSONObject;

;

public class JsonHandler {

    private int bpm;
    private double lat;
    private double lon;
    private String status;
    private long timestamp;
    private String crc32;

    public static JsonHandler processJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);

            JsonHandler data = new JsonHandler();
            data.bpm = obj.getInt("bpm");
            data.lat = obj.getDouble("lat");
            data.lon = obj.getDouble("lon");
            data.status = obj.getString("status");
            data.timestamp = obj.getLong("timestamp");
            data.crc32 = obj.getString("crc32");

            return data;

        } catch (Exception e) {
            System.err.println("Invalid JSON or parsing error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        switch(status){
            case "null": status = "Normal/Resposta por obter"; break;
            case "false": status = "CANCELADO!"; break;
            case "true": status = "EMERGENCIA!"; break;
        }

        return String.format(
                "❤️ BPM: %d\n📍 Location: (%.6f, %.6f)\n🔌 Status: %s\n🕒 Timestamp: %d\n📦 CRC32: %s",
                bpm, lat, lon, status, timestamp, crc32
        );
    }
}