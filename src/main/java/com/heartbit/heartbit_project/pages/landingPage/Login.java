package com.heartbit.heartbit_project.pages.landingPage;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {

    public static void login(String loginEmail, String loginPassword) {
        //Validações

        loginEmail = loginEmail.trim();
        loginPassword = loginPassword.trim();

        if (!loginEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$")) {
            System.out.println("Invalid email address.");
        }
        else if (loginPassword.isEmpty()) {
            System.out.println("Password is empty.");
        }
        else {
            String url = "jdbc:mysql://localhost:3306/pcmr";
            String username = "root";
            String password = "";

            try {
                Connection con = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = con.prepareStatement("select user_name, user_password from `user` where user_email = ?");
                statement.setString(1, loginEmail);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    String pass = rs.getString("user_password");

                    if (BCrypt.checkpw(loginPassword, pass)) {
                        System.out.println("Logged in successfully.");
                    } else {
                        System.out.println("Incorrect password.");
                    }
                } else {
                    System.out.println("Incorrect email.");
                }
                con.close();
            }
            catch (Exception e) {
                System.out.println("Connection error: " + e.getMessage());
            }
        }

        //Errado: Toast
        //Certo:
        //Verifica dados tabela user
        //Errado: Dados incorretos
        //Certo: Redireciona HomePage
    }
}
