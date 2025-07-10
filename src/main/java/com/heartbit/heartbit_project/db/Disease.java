package com.heartbit.heartbit_project.db;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.*;
import java.util.*;

public class Disease {

    private ArrayList<String> diseases;
    private final Dotenv dotenv = Dotenv.load();
    private final User user;
    private final String dbUrl = dotenv.get("DB_URL");
    private final String dbUsername = dotenv.get("DB_USERNAME");
    private final String dbPass = dotenv.get("DB_PASSWORD");

    public Disease(ArrayList<String> diseases, User user) {
        this.diseases = diseases;
        this.user = user;
    }

    public Disease(User user) {
        this.user = user;
        this.diseases = new ArrayList<>();
    }

    public ArrayList<String> getDiseases() {
        return diseases;
    }

    public void setDiseases(ArrayList<String> diseases) {
        this.diseases = diseases;
    }

    public ArrayList<String> getUserDiseases() throws SQLException, ClassNotFoundException {
        String selectSql = "SELECT disease_name FROM disease WHERE disease_user_id = ?";
        ArrayList<String> diseases = new ArrayList<>();

        // Ensure driver is loaded
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPass);
             PreparedStatement ps = con.prepareStatement(selectSql)) {

            ps.setInt(1, user.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    diseases.add(rs.getString("disease_name"));
                }
            }
        }

        return diseases;
    }


    public String syncUserDiseases() {
        String message = "";
        String selectSql = "SELECT disease_name FROM disease WHERE disease_user_id = ?";
        String insertSql = "INSERT INTO disease(disease_user_id, disease_name) VALUES (?, ?)";
        String deleteSql = "DELETE FROM disease WHERE disease_user_id = ? AND disease_name = ?";

        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbUrl, dbUsername, dbPass);
            con.setAutoCommit(false);

            // 1) Load existing diseases
            Set<String> existing = new HashSet<>();
            try (PreparedStatement selectPs = con.prepareStatement(selectSql)) {
                selectPs.setInt(1, user.getId());
                try (ResultSet rs = selectPs.executeQuery()) {
                    while (rs.next()) {
                        existing.add(rs.getString("disease_name"));
                    }
                }
            }

            // 2) Compute additions/removals
            Set<String> toAdd = new HashSet<>(diseases);
            toAdd.removeAll(existing);

            Set<String> toDelete = new HashSet<>(existing);
            diseases.forEach(toDelete::remove);

            if (!toAdd.isEmpty()) {
                try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                    for (String name : toAdd) {
                        insertPs.setInt(1, user.getId());
                        insertPs.setString(2, name);
                        insertPs.addBatch();
                    }
                    insertPs.executeBatch();
                }
            }

            if (!toDelete.isEmpty()) {
                try (PreparedStatement deletePs = con.prepareStatement(deleteSql)) {
                    for (String name : toDelete) {
                        deletePs.setInt(1, user.getId());
                        deletePs.setString(2, name);
                        deletePs.addBatch();
                    }
                    deletePs.executeBatch();
                }
            }
            con.commit();

        } catch (SQLIntegrityConstraintViolationException e) {
            message = "Error: Attempted duplicate insert.";
            if (con != null) try { con.rollback(); } catch (SQLException ignore) {}
        } catch (SQLException e) {
            message = "Database error: " + e.getMessage();
            if (con != null) try { con.rollback(); } catch (SQLException ignore) {}
        } catch (ClassNotFoundException e) {
            message = "JDBC Driver not found.";
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ignore) {}
            }
        }

        return message;
    }
}
