package com.heartbit.heartbit_project.visual_functions;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class Images {

    private static final String pakage = "/com/heartbit/heartbit_project";

    public static void HomeEnabled(ImageView homeImg, ImageView accountImg, ImageView resultsImg, ImageView bluetoothImg) {
        homeImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/enable/Home.png")).toExternalForm()));
        accountImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Person.png")).toExternalForm()));
        resultsImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Results.png")).toExternalForm()));
        bluetoothImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Bluetooth.png")).toExternalForm()));
    }

    public static void AccountEnabled(ImageView homeImg, ImageView accountImg, ImageView resultsImg, ImageView bluetoothImg) {
        homeImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Home.png")).toExternalForm()));
        accountImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/enable/Person.png")).toExternalForm()));
        resultsImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Results.png")).toExternalForm()));
        bluetoothImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Bluetooth.png")).toExternalForm()));
    }

    public static void ResultsEnabled(ImageView homeImg, ImageView accountImg, ImageView resultsImg, ImageView bluetoothImg) {
        homeImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Home.png")).toExternalForm()));
        accountImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Person.png")).toExternalForm()));
        resultsImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/enable/Results.png")).toExternalForm()));
        bluetoothImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Bluetooth.png")).toExternalForm()));
    }

    public static void BluetoothEnabled(ImageView homeImg, ImageView accountImg, ImageView resultsImg, ImageView bluetoothImg) {
        homeImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Home.png")).toExternalForm()));
        accountImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Person.png")).toExternalForm()));
        resultsImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Results.png")).toExternalForm()));
        bluetoothImg.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/enable/Bluetooth.png")).toExternalForm()));
    }
}
