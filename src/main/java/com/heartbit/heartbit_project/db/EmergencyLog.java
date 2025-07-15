package com.heartbit.heartbit_project.db;

import java.sql.*;
import java.util.ArrayList;

public class EmergencyLog {
    private final HeartEnv heartEnv = new HeartEnv();
    private Bpm bpm;
    private int hasContacted;
    private String lat;
    private String lon;
    private ArrayList<EmergencyReading> emergencyReadings;

    public EmergencyLog(){
        this.emergencyReadings = new ArrayList<>();
    }

    public EmergencyLog(Bpm bpm, int hasContacted, String lat, String lon) {
        this.bpm = bpm;
        this.hasContacted = hasContacted;
        this.lat = lat;
        this.lon = lon;
    }

    public Bpm getBpm() {
        return bpm;
    }
    public int getHasContacted() {
        return hasContacted;
    }
    public String getLat() {
        return lat;
    }
    public String getLon() {
        return lon;
    }
    public ArrayList<EmergencyReading> getEmergencyReadings() {
        return emergencyReadings;
    }

    public void insertEmergencyData(){
        String insert = "INSERT INTO emergency_log (el_bpm_id, el_has_contacted, el_lat, el_lon) VALUES (?, ?, ?,?)";
        try (Connection con = DriverManager.getConnection(heartEnv.getDbUrl(), heartEnv.getDbUsername(), heartEnv.getDbPass());
             PreparedStatement ps = con.prepareStatement(insert)) {
            ps.setInt(1, getBpm().getId());
            ps.setInt(2, getHasContacted());
            ps.setString(3, getLat());
            ps.setString(4, getLon());
            ps.executeUpdate();
        }
        catch (Exception e) {
            System.err.println("Error: "+e.getMessage());
            e.printStackTrace();
        }

    }

    public void getEmergencyReadingsForUser(int userId) throws SQLException {
        String sql = """
            SELECT b.bpm_value, e.el_lat, e.el_lon, b.bpm_datetime, e.el_has_contacted
            FROM bpm b
            JOIN emergency_log e ON e.el_bpm_id = b.bpm_id
            WHERE b.bpm_user_id = ?
            """;

        try (Connection conn = DriverManager.getConnection(heartEnv.getDbUrl(), heartEnv.getDbUsername(), heartEnv.getDbPass());
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            ArrayList<EmergencyReading> readings = new ArrayList<>();
            while (rs.next()) {
                int value = rs.getInt("bpm_value");
                String lat = rs.getString("el_lat");
                String lon = rs.getString("el_lon");
                Timestamp dt = rs.getTimestamp("bpm_datetime");
                int has_contacted = rs.getInt("el_has_contacted");
                readings.add(new EmergencyReading(value, lat, lon, dt, has_contacted));
            }
            emergencyReadings = readings;
        }
    }


}
