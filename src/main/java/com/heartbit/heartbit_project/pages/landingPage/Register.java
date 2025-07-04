package com.heartbit.heartbit_project.pages.landingPage;
import org.mindrot.jbcrypt.BCrypt;
import javax.crypto.*;
import java.sql.*;

public class Register {

    String registerName;
    String registerEmail;
    String registerPassword;
    String registerPasswordConfirm;

    public Register(String registerName, String registerEmail, String registerPassword, String registerPasswordConfirm) {
        this.registerName = registerName;
        this.registerEmail = registerEmail;
        this.registerPassword = registerPassword;
        this.registerPasswordConfirm = registerPasswordConfirm;
    }

    public String validateRegisterForm() {
        //Validações
        //Errado: Toast
        //Certo:
        //Inserir dados tabela user
        //Redireciona HomePage

        if (registerName.isEmpty()) {
            return "Name field is empty.";
        }
        else if (!registerEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return "Invalid e-mail address.";
        }
        else if (!(registerPassword.matches("^(?=.*[!@#$%^&*()_\\-+=\\[\\]{};':\"\\\\|,.<>/?])(?=.*\\d).{12,}$"))) {
            return "Password is not valid. Check password requirements.";
        }

        else if (!registerPassword.matches(registerPasswordConfirm)) {
            return "Passwords do not match.";
        }
        else {
            return "";
        }
    }

    public String insertDataRegister() {
        String hashedPassword = BCrypt.hashpw(registerPassword, BCrypt.gensalt(12));
        String message = "";

        try {

        String url = "jdbc:mysql://localhost:3306/pcmr";
        String username = "root";
        String password = "";
        String insert = "INSERT INTO `user`(user_name, user_email, user_password, user_phone, user_eme_phone) VALUES (?, ?, ?, ?, ?)";

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = con.prepareStatement(insert);
            ps.setString(1, registerName);
            ps.setString(2, registerEmail);
            ps.setString(3, hashedPassword);
            ps.setString(4, "NULL");
            ps.setString(5, "NULL");

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
//                message = "Registration successful!";
            } else {
                message = "Registration failed. No rows affected.";
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            message = "Error: Email already exists.";
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
