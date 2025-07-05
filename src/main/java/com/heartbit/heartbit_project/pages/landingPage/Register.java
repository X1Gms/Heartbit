package com.heartbit.heartbit_project.pages.landingPage;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class Register {

    String name;
    String email;
    String password;
    String passwordConfirm;
    String phoneNumber;
    String emergencyPhoneNumber;

    public Register(String name, String email, String password, String passwordConfirm) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setEmergencyPhoneNumber(String emergencyPhoneNumber) {
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    public String validateRegisterForm() {
        //Validações
        //Errado: Toast
        //Certo:
        //Inserir dados tabela user
        //Redireciona HomePage

        if (name.isEmpty()) {
            return "Name field is empty.";
        }
        else if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return "Invalid e-mail address.";
        }
        else if (!(password.matches("^(?=.*[!@#$%^&*()_\\-+=\\[\\]{};':\"\\\\|,.<>/?])(?=.*\\d).{12,}$"))) {
            return "Password is not valid. Check password requirements.";
        }

        else if (!password.matches(passwordConfirm)) {
            return "Passwords do not match.";
        }
        else {
            return "";
        }
    }

    public String validatePhoneForm() {
        String regex = "^\\+\\d{1,3}(?:[ -]\\d{2,4}){2,4}$";

        if (!phoneNumber.matches(regex)) {
            return "Invalid Personal Phone Number.";
        } else if (!emergencyPhoneNumber.matches(regex)) {
            return "Invalid Emergency Phone Number.";
        } else {
            return "";
        }
    }


    public String insertDataRegister() {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        String message = "";

        try {

        String url = "jdbc:mysql://localhost:3306/pcmr";
        String username = "root";
        String password = "";
        String insert = "INSERT INTO `user`(user_name, user_email, user_password, user_phone, user_eme_phone) VALUES (?, ?, ?, ?, ?)";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
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

}
