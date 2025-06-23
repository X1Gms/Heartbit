package com.heartbit.heartbit_project;

import com.heartbit.heartbit_project.visual_functions.Images;
import com.heartbit.heartbit_project.visual_functions.Transitions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;

public class HelloController {
    @FXML
    private Pane myPane;
    @FXML
    private VBox LoginForm;
    @FXML
    private VBox RegisterForm;
    @FXML
    private ImageView homeImg;
    @FXML
    private ImageView accountImg;
    @FXML
    private ImageView resultsImg;
    @FXML
    private VBox dialogBg;
    @FXML
    private VBox sidebar;

    //Login/Register
    @FXML
    private void AppearLogin(ActionEvent event) {
        Transitions.FadeOutIn(myPane, LoginForm,650, 500);
    }

    @FXML
    private void AppearRegister(MouseEvent event) {
        Transitions.FadeOutIn(LoginForm, RegisterForm,650, 500);
    }

    @FXML
    private void AppearLoginAgain(MouseEvent event) {
        Transitions.FadeOutIn(RegisterForm, LoginForm,650, 500);
    }

    //Sidebar
    //Is Enabled Functions

    @FXML
    private void HomeEnabled(MouseEvent event) {
        Images.HomeEnabled(homeImg,accountImg,resultsImg);
        disSidebar();
    }

    @FXML
    private void AccountEnabled(MouseEvent event) {
        Images.AccountEnabled(homeImg,accountImg,resultsImg);
        disSidebar();
    }

    @FXML
    private void ResultsEnabled(MouseEvent event) {
        Images.ResultsEnabled(homeImg,accountImg,resultsImg);
        disSidebar();
    }

    @FXML
    private void enSideBar(MouseEvent event) {
        Transitions.FadeIn(sidebar,400, Transitions.Direction.TO_RIGHT, 500);
        Transitions.FadeIn(dialogBg,400, Transitions.Direction.TO_BOTTOM, 0);
    }

    @FXML
    private void OnDisSideBar(MouseEvent event) {
        disSidebar();
    }

    private void disSidebar(){
        Transitions.FadeOut(sidebar,500, Transitions.Direction.TO_RIGHT, 500);
        Transitions.FadeOut(dialogBg,500, Transitions.Direction.TO_TOP, 0);
    }
}