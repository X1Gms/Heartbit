package com.heartbit.heartbit_project.pages.landingPage;

import com.heartbit.heartbit_project.Validations;
import io.github.cdimascio.dotenv.Dotenv;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class User {

    String name;
    String email;
    String password;
    String passwordConfirm;
    String phoneNumber;
    String emergencyPhoneNumber;
    Dotenv dotenv = Dotenv.load();

    String dbUrl = dotenv.get("DB_URL");
    String dbUsername = dotenv.get("DB_USERNAME");
    String dbPass = dotenv.get("DB_PASSWORD");

    public User(String name, String email, String password, String passwordConfirm) {
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
        else if (!Validations.isValidEmail(email)) {
            return "Invalid e-mail address.";
        }
        else if (!Validations.isValidPassword(password)) {
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
        if (!Validations.isValidPhoneNumber(phoneNumber)) {
            return "Invalid Personal Phone Number.";
        } else if (!Validations.isValidPhoneNumber(emergencyPhoneNumber)) {
            return "Invalid Emergency Phone Number.";
        } else if (phoneNumber.matches(emergencyPhoneNumber)) {
            return "Phone numbers can't be the same.";
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

    public String Login() {
        if (!Validations.isValidEmail(email)) {
            return "Invalid email address.";
        }
        else if (password.isEmpty()) {
            return "Password is empty.";
        }
        else {
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
            }
            catch (Exception e) {
                System.out.println("Connection Error: " + e.getMessage());
                return "Unexpected Error. Try Again.";
            }
        }

        //Errado: Toast
        //Certo:
        //Verifica dados tabela user
        //Errado: Dados incorretos
        //Certo: Redireciona HomePage
    }

    public String editProfile()
    {
        if (name.isEmpty()) {
            return "Name field is empty.";
        }
        else if (!Validations.isValidEmail(email)) {
            return "Incorrect email address.";
        }
        else if (!Validations.isValidPassword(password)) {
            return "Invalid Password.";
        }
        else if (!password.matches(passwordConfirm)) {
            return "Passwords don't match.";
        }
        else if (phoneNumber.matches(emergencyPhoneNumber)) {
            return "Phone numbers can't be the same.";
        }
        else if (!(Validations.isValidPhoneNumber(phoneNumber)) && (Validations.isValidPhoneNumber(emergencyPhoneNumber))) {
            return "Invalid phone number.";
        }
        else {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
            String message = "";

            try {
                Connection con = DriverManager.getConnection(dbUrl, dbUsername, dbPass);
                PreparedStatement statement = con.prepareStatement("update `user` set user_name = ?, user_email = ?, user_password = ?, user_phone = ?, user_eme_phone where user_email = ?");

                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, hashedPassword);
                statement.setString(4, phoneNumber);
                statement.setString(5, emergencyPhoneNumber);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
//                  message = "Registration successful!";
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
                return "Unexpected Error. Try Again.";
            }
            return message;
        }
    }
}