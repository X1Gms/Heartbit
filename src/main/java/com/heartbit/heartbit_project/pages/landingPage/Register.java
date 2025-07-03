package com.heartbit.heartbit_project.pages.landingPage;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Register {

    public static void register(String registerName, String registerEmail, String registerPassword, String registerPasswordConfirm) {
        //Validações

        registerName = registerName.trim();
        registerEmail = registerEmail.trim();
        registerPassword = registerPassword.trim();
        registerPasswordConfirm = registerPasswordConfirm.trim();

        if (registerName.isEmpty()) {
            System.out.println("Insert a name for the register.");
        }
        else if (!registerEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$")) {
            System.out.println("Incorrect email address.");
        }
        else if (!(registerPassword.length() < 12 && registerPassword.contains("^[0-9._%+-]"))) {
            System.out.println("Password is not valid.");
        }

        else if (!registerPassword.matches(registerPasswordConfirm)) {
            System.out.println("Password confirmation is not valid.");
        }
        else {
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

        //Errado: Toast

        //Certo:
        //Inserir dados tabela user
        //Redireciona HomePage

    }
}
