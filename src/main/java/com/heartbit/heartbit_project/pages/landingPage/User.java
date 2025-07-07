package com.heartbit.heartbit_project.pages.landingPage;

import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class User {

    private String name;
    private String email;
    private String password;
    private String passwordConfirm;
    private String phoneNumber;
    private String emergencyPhoneNumber;
    private final Dotenv dotenv = Dotenv.load();
    private int id;

    private final String dbUrl = dotenv.get("DB_URL");
    private final String dbUsername = dotenv.get("DB_USERNAME");
    private final String dbPass = dotenv.get("DB_PASSWORD");

    private final String rgxEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final String rgxPassword = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=.*[a-zA-Z]).{12,}$";
    private final String rgxPhone = "^\\+(?=(?:.*\\d){7,15}$)[0-9][0-9\\- ]*\\d$";

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(int id, String name, String email, String phoneNumber, String emergencyPhoneNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, String passwordConfirm, String phoneNumber, String emergencyPhoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.phoneNumber = phoneNumber;
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmergencyPhoneNumber() {
        return emergencyPhoneNumber;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmergencyPhoneNumber(String emergencyPhoneNumber) {
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    public String validateRegisterForm() {
        if (getName().isEmpty()) {
            return "Name field is empty.";
        } else if (!getEmail().matches(rgxEmail)) {
            return "Invalid e-mail address.";
        } else if (!getPassword().matches(rgxPassword)) {
            return "Password is not valid. Check password requirements.";
        } else if (!getPassword().equals(getPasswordConfirm())) {
            return "Passwords do not match.";
        } else {
            return "";
        }
    }

    public String validatePhoneForm() {
        String phone = getPhoneNumber();
        String emergencyPhone = getEmergencyPhoneNumber();

        if (phone == null || phone.trim().isEmpty()) {
            return "Phone Number is required.";
        } else if (emergencyPhone == null || emergencyPhone.trim().isEmpty()) {
            return "Emergency Number is required.";
        } else if (phone.trim().equals(emergencyPhone.trim())) {
            return "Phone numbers can't be the same.";
        } else if (!phone.matches(rgxPhone)) {
            return "Invalid Phone Number format.";
        } else if (!emergencyPhone.matches(rgxPhone)) {
            return "Invalid Emergency Number format.";
        } else {
            return "";
        }
    }


    public String insertDataRegister() {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String message = "";

        try {
            String insert = "INSERT INTO `user`(user_name, user_email, user_password, user_phone, user_eme_phone) VALUES (?, ?, ?, ?, ?)";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPass);
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);
            ps.setString(4, phoneNumber);
            ps.setString(5, emergencyPhoneNumber);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
//                message = "Registration successful!";
            } else {
                message = "Registration failed. No rows affected.";
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            message = "Error: Email or Phone Number already exists.";
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            message = "Database Error";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            message = "Unexpected Error. Try Again.";
        }

        return message;
    }

    public User getUserDetails() {
        String sql = "SELECT user_id, user_name, user_email, user_phone, user_eme_phone FROM user WHERE user_email = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email); // Set parameter

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int user_id = rs.getInt("user_id");
                    String user_name = rs.getString("user_name");
                    String user_email = rs.getString("user_email");
                    String user_phone = rs.getString("user_phone");
                    String user_eme_phone = rs.getString("user_eme_phone");
                    return new User(user_id, user_name, user_email, user_phone, user_eme_phone);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String Login() {
        if (!email.matches(rgxEmail)) {
            return "Invalid email address.";
        } else if (password.isEmpty()) {
            return "Password is empty.";
        } else {
            try {
                Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPass);
                PreparedStatement statement = con.prepareStatement("select user_name, user_password from `user` where user_email = ?");
                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    String BDpass = rs.getString("user_password");

                    if (BCrypt.checkpw(password, BDpass)) {
                        con.close();
                        return "";
                    } else {
                        con.close();
                        return "Incorrect password.";
                    }
                } else {
                    con.close();
                    return "Incorrect email.";
                }
            } catch (Exception e) {
                System.out.println("Connection Error: " + e.getMessage());
                return "Unexpected Error. Try Again.";
            }
        }
    }

    public String editProfile() {
        System.out.println(getPhoneNumber() + "\n"+getEmergencyPhoneNumber());
        if (getName().isEmpty()) {
            return "Name field is empty.";
        } else if (!getEmail().matches(rgxEmail)) {
            return "Invalid e-mail address.";
        }else if (!validatePhoneForm().isEmpty()) {
            return validatePhoneForm();
        }else if (getPassword().isEmpty()) {

        } else if (!getPassword().matches(rgxPassword)) {
            return "Password is not valid. Check password requirements.";
        }

        String storedHash;
        try (Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPass);
             PreparedStatement ps = con.prepareStatement(
                     "SELECT user_password FROM `user` WHERE user_id = ?"
             )) {
            ps.setInt(1, getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return "User not found.";
                }
                storedHash = rs.getString("user_password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error.";
        }

        // 3) Verify current (old) password
        // passwordConfirm is the user’s old password input
        if (!BCrypt.checkpw(passwordConfirm, storedHash)) {
            return "Current password is incorrect.";
        }

        // 4) Prepare the password to save
        String passwordToSave = storedHash;
        if (!password.isEmpty()) {
            // password is the new password input
            // passwordConfirmNew is the confirmation of the new password
            if (!password.matches(rgxPassword)) {
                return "New password does not meet complexity requirements.";
            }
            // **Hash the new password once** and store it
            passwordToSave = BCrypt.hashpw(password, BCrypt.gensalt(12));
        }

        // 5) Update row (include the old hash in WHERE to detect concurrent changes)
        String sql = """
        
                UPDATE `user`
           SET user_name      = ?,
               user_email     = ?,
               user_password  = ?,
               user_phone     = ?,
               user_eme_phone = ?
         WHERE user_id       = ?
           AND user_password = ?
        """;

        try (Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPass);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, getName());
            ps.setString(2, getEmail());
            ps.setString(3, passwordToSave);       // uses either old or newly hashed
            ps.setString(4, getPhoneNumber());
            ps.setString(5, getEmergencyPhoneNumber());
            ps.setInt(6, getId());
            ps.setString(7, storedHash);           // ensure password hasn’t changed

            int rows = ps.executeUpdate();
            if (rows == 0) {
                return "Failed to update profile (maybe password changed elsewhere).";
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            return "Email or phone number already in use.";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error.";
        }

        return "";
    }
}