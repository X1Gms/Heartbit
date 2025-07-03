module com.heartbit.heartbit_project {
    requires jbcrypt;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    opens com.heartbit.heartbit_project to javafx.fxml;
    opens com.heartbit.heartbit_project.components to javafx.fxml;

    exports com.heartbit.heartbit_project;
    exports com.heartbit.heartbit_project.components;
}