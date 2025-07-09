module com.heartbit.heartbit_project {
    requires jbcrypt;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.eclipse.paho.client.mqttv3;
    requires io.github.cdimascio.dotenv.java;
    requires org.json;
    requires jakarta.mail;

    opens com.heartbit.heartbit_project to javafx.fxml;
    opens com.heartbit.heartbit_project.components to javafx.fxml;

    exports com.heartbit.heartbit_project;
    exports com.heartbit.heartbit_project.components;
}