package com.heartbit.heartbit_project.db;

import java.sql.*;

public class Bpm {
    private int id;
    private User user;
    private int value;
    private Timestamp dateTime;
    private final HeartEnv heartEnv = new HeartEnv();
    private int avgBPM;
    private int maxBPM;
    private int minBPM;

    public Bpm(){

    }

    public Bpm(User user, int value) {
        this.user = user;
        this.value = value;
        this.dateTime = new Timestamp(System.currentTimeMillis());
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public int getValue() {
        return value;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public int getAvgBPM() {
        return avgBPM;
    }

    public int getMaxBPM() {
        return maxBPM;
    }

    public int getMinBPM() {
        return minBPM;
    }

    public int insertBpmData() {
        String insert = "INSERT INTO bpm (bpm_user_id, bpm_value, bpm_datetime) VALUES (?, ?, ?)";
        try (Connection con = DriverManager.getConnection(heartEnv.getDbUrl(), heartEnv.getDbUsername(), heartEnv.getDbPass());
             PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {  // <<== IMPORTANT

            ps.setInt(1, getUser().getId());
            ps.setInt(2, getValue());
            ps.setTimestamp(3, getDateTime());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                    // Save or use the id as needed
                } else {
                    throw new SQLException("Insert succeeded but no ID returned.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public void getBpmStatus(int userId) throws SQLException {
        String sql = """
            SELECT AVG(bpm_value) AS avg_bpm,
                   MIN(bpm_value) AS min_bpm,
                   MAX(bpm_value) AS max_bpm
            FROM bpm
            WHERE bpm_user_id = ?
            """;

        try (Connection conn = DriverManager.getConnection(heartEnv.getDbUrl(), heartEnv.getDbUsername(), heartEnv.getDbPass());
              PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                        avgBPM = rs.getInt("avg_bpm");
                        maxBPM = rs.getInt("max_bpm");
                        minBPM = rs.getInt("min_bpm");
            }
        }
    }


}
