package com.heartbit.heartbit_project.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.CRC32;

public class JsonHandler {

    private static Integer bpm;
    private static Double lat;
    private static Double lon;
    private static String status;
    private static Boolean hasCache;
    private static Long timestamp;
    String crc32;
    private List<DataPoint> dataPoints;

    public JsonHandler(){}

    public static int getBpm() {
        return bpm;
    }

    public static Double getLat() {
        return lat;
    }

    public static Double getLon() {
        return lon;
    }

    public static String getStatus() {
        return status;
    }

    public static Long getTimestamp() {
        return timestamp;
    }

    public static JsonHandler processJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
                JsonHandler handler = new JsonHandler();

            handler.bpm = obj.has("bpm") ? obj.getInt("bpm") : null;
            handler.lat = obj.has("lat") ? obj.getDouble("lat") : null;
            handler.lon = obj.has("lon") ? obj.getDouble("lon") : null;
            handler.status = obj.optString("status", null);
            handler.hasCache = obj.has("hasCache") ? obj.getBoolean("hasCache") : null;
            handler.timestamp = obj.has("timestamp") ? obj.getLong("timestamp") : null;
            handler.crc32 = obj.optString("crc32", null); // Always as string

            // If "data" array exists, parse batch data
            if (obj.has("data")) {
                handler.dataPoints = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("data");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject item = dataArray.getJSONObject(i);
                    int itemBpm = item.getInt("bpm");
                    long millis = item.getLong("millis");
                    handler.dataPoints.add(new DataPoint(itemBpm, millis));
                }
            }

            return handler;
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            return null;
        }
    }

    public static String calculateMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());

            // Convert the byte array to a hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b & 0xff));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }


    public static List<DataPoint> processOfflineData(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            if (!obj.has("data")) return Collections.emptyList();

            JSONArray dataArray = obj.getJSONArray("data");
            List<DataPoint> points = new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                int bpm = item.getInt("bpm");
                long millis = item.getLong("milis");
                points.add(new DataPoint(bpm, millis));
            }
            return points;
        } catch (Exception e) {
            System.err.println("Error parsing offline data: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public String toString() {
            if(bpm == null){
                return "true";
            }
        System.out.println(status);

        String statusStr = switch (status) {
            case "1" -> "CANCELADO!";
            case "4" -> "EMERGENCIA!!";
            default -> "Normal/Resposta por obter";
        };

        String sim = hasCache != null && hasCache ? "✅" : "❌";

        if (dataPoints != null && !dataPoints.isEmpty()) {
            StringBuilder sb = new StringBuilder("📊 Batch Data Points:\n");
            for (DataPoint dp : dataPoints) {
                sb.append(String.format("❤️ BPM: %d at %d ms\n", dp.bpm(), dp.millis()));
            }
            sb.append("📦 CRC32: ").append(crc32 != null ? crc32 : "N/A");
            return sb.toString();
        }

        return String.format(
                "❤️ BPM: %s\n📍 Location: (%.6f, %.6f)\n🔌 Status: %s\n🕒 Timestamp: %s\n📦 CRC32: %s\n🧪 hasCache: %s",
                bpm != null ? bpm : "N/A",
                lat != null ? lat : 0.0,
                lon != null ? lon : 0.0,
                statusStr != null ? statusStr : "N/A",
                timestamp != null ? timestamp : "N/A",
                crc32 != null ? crc32 : "N/A",
                sim
        );
    }

    public Boolean getHasCache() {
        return hasCache;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public static String generateCommandPrompt(String command) {
        try {
            // Step 1: Build initial JSON object
            JSONObject json = new JSONObject();
            json.put("command", command);

            // Step 2: Convert to string and calculate CRC32
            String jsonString = json.toString();
            CRC32 crc = new CRC32();
            crc.update(jsonString.getBytes());
            String crcValue = String.format("0x%08X", crc.getValue());

            // Step 3: Add CRC32 to JSON
            json.put("crc32", crcValue);

            // Step 4: Return final JSON string
            return json.toString();
        } catch (Exception e) {
            System.err.println("Failed to generate offline command with CRC: " + e.getMessage());
            return null;
        }

    }
}
