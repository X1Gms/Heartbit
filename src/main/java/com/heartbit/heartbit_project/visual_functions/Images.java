package com.heartbit.heartbit_project.visual_functions;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Objects;

public class Images {

    private static final String pakage = "/com/heartbit/heartbit_project";

    public static void HomeEnabled(ImageView image1, ImageView image2, ImageView image3) {
        image1.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/enable/Home.png")).toExternalForm()));
        image2.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Person.png")).toExternalForm()));
        image3.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Results.png")).toExternalForm()));
    }

    public static void AccountEnabled(ImageView image1, ImageView image2, ImageView image3) {
        image1.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Home.png")).toExternalForm()));
        image2.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/enable/Person.png")).toExternalForm()));
        image3.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Results.png")).toExternalForm()));
    }

    public static void ResultsEnabled(ImageView image1, ImageView image2, ImageView image3) {
        image1.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Home.png")).toExternalForm()));
        image2.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/normal/Person.png")).toExternalForm()));
        image3.setImage(new Image(Objects.requireNonNull(Images.class.getResource(pakage+"/images/icons/enable/Results.png")).toExternalForm()));
    }
}
