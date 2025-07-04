package com.heartbit.heartbit_project.pages.landingPage;
import org.mindrot.jbcrypt.BCrypt;
import javax.crypto.*;
import java.sql.*;

public class Register {

    public static String validateRegisterForm(String registerName, String registerEmail, String registerPassword, String registerPasswordConfirm) {
        //Validações

        if (registerName.isEmpty()) {
            return "Name field is empty";
        }
        else if (!registerEmail.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return "Invalid e-mail address.";
        }
        else if (!(registerPassword.matches("^(?=.*[!@#$%^&*()_\\-+=\\[\\]{};':\"\\\\|,.<>/?])(?=.*\\d).{12,}$"))) {
            return "Password is not valid. Check password requirements";
        }

        else if (!registerPassword.matches(registerPasswordConfirm)) {
            return "Passwords do not match";
        }
        else {
            return "";
        }
        //Errado: Toast

        //Certo:
        //Inserir dados tabela user
        //Redireciona HomePage

    }

    public static void insertDataRegister(String registerName, String registerEmail, String registerPassword, String registerPasswordConfirm) {
        registerName = registerName.trim();
        registerEmail = registerEmail.trim();
        registerPassword = (BCrypt.hashpw(registerPassword, BCrypt.gensalt(12)));
        String url = "jdbc:mysql://localhost:3306/pcmr";
        String username = "root";
        String password = "";

        try {
            Connection con = DriverManager.getConnection(url, username, password);
            String insert = "insert into `user`(user_name, user_email, user_password, user_phone, user_eme_phone) values (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(insert); {
                ps.setString(1, registerName);
                ps.setString(2, registerEmail);
                ps.setString(3, registerPassword);
                ps.setInt(4, 0);
                ps.setInt(5, 0);
                ps.executeUpdate();
            }
            ps.close();
            System.out.println("New insertion successful.");
            con.close();
        }
        catch(Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }
}
