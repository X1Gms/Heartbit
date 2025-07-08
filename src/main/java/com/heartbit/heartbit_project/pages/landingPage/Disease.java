package com.heartbit.heartbit_project.pages.landingPage;
import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.*;

public class Disease {

    private ArrayList<String> disease;
    private final Dotenv dotenv = Dotenv.load();

    private final String dbUrl = dotenv.get("DB_URL");
    private final String dbUsername = dotenv.get("DB_USERNAME");
    private final String dbPass = dotenv.get("DB_PASSWORD");

    public Disease(ArrayList<String> disease) {
        this.disease = disease;
    }

    public ArrayList<String> getDisease() {
        return disease;
    }

    public void setDisease(ArrayList<String> disease) {
        this.disease = disease;
    }

    public ArrayList<String> getDataDisease(List<String> getValues) {
        return disease;
    }

    public String insertDataDisease(User user) {
        String message = "";

        try {
            String insert = "INSERT INTO `disease`(disease_user_id, disease_name) VALUES (?, ?)";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPass);
            PreparedStatement ps = con.prepareStatement(insert);
            for (int i = 0; i < disease.size(); i++) {
                ps.setInt(1, user.getId());
                ps.setString(2, String.valueOf(disease));
            }
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
//              message = "Registration successful!";
            } else {
                message = "Registration failed. No rows affected.";
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            message = "Error: Disease already exists.";
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            message = "Database Error";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            message = "Unexpected Error. Try Again.";
        }
        return message;
    }

    /* Disease disease = new Disease(); */
}
