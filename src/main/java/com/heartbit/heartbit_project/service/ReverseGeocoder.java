package com.heartbit.heartbit_project.service;

import com.google.gson.*;
import com.heartbit.heartbit_project.db.HeartEnv;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ReverseGeocoder {

    public record LocationInfo(String address, String city) {}

    private final HeartEnv env = new HeartEnv();

    public LocationInfo lookup(double lat, double lon) {
        String addr = "Unknown";
        String city = "Unknown";
        try {
            String endpoint = String.format(
                    "https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s&language=pt",
                    lat, lon, env.getGoogle_API());

            HttpURLConnection con = (HttpURLConnection) new URL(endpoint).openConnection();
            con.setRequestMethod("GET");

            try (InputStream is = con.getInputStream(); Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                JsonObject root = JsonParser.parseReader(rd).getAsJsonObject();
                JsonArray results = root.getAsJsonArray("results");
                if (results != null && !results.isJsonNull() && results.size() > 0) {
                    JsonObject first = results.get(0).getAsJsonObject();
                    addr = first.get("formatted_address").getAsString();

                    JsonArray comps = first.getAsJsonArray("address_components");
                    for (JsonElement compEl : comps) {
                        JsonObject comp = compEl.getAsJsonObject();
                        for (JsonElement t : comp.getAsJsonArray("types")) {
                            String type = t.getAsString();
                            if ("locality".equals(type)) {
                                city = comp.get("long_name").getAsString();
                                break;
                            }
                        }
                        if (!"Desconhecida".equals(city)) break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Reverse geocoding failed: " + e.getMessage());
        }
        return new LocationInfo(addr, city);
    }

    public String cityFor(double lat, double lon) {
        return lookup(lat, lon).city();
    }
}