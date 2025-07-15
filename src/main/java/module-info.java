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
    requires itextpdf;
    requires com.google.gson;

    opens com.heartbit.heartbit_project to javafx.fxml;
    opens com.heartbit.heartbit_project.components to javafx.fxml;

    exports com.heartbit.heartbit_project;
    exports com.heartbit.heartbit_project.components;
    exports com.heartbit.heartbit_project.network;
    opens com.heartbit.heartbit_project.network to javafx.fxml;
}