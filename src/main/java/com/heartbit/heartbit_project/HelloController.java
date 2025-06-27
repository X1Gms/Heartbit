package com.heartbit.heartbit_project;

import com.heartbit.heartbit_project.components.MultiDropdown;
import com.heartbit.heartbit_project.visual_functions.Images;
import com.heartbit.heartbit_project.visual_functions.Transitions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class HelloController implements Initializable {

    @FXML
    private Pane myPane;
    @FXML
    private VBox LoginForm;
    @FXML
    private VBox landingPage;
    @FXML
    private VBox homePage;
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
    @FXML
    private FlowPane home;
    @FXML
    private FlowPane account;
    @FXML
    private FlowPane results;
    @FXML
    private FlowPane bluetooth;
    @FXML
    private ImageView bluetoothImg;
    @FXML
    private MultiDropdown multiDropdown;
    @FXML
    private FlowPane editAccount;
    @FXML
    private VBox editDiseases;
    @FXML
    private Pane paneEditDiseases;
    @FXML
    private Pane paneEditAccount;
    @FXML
    private HBox sucessToast;

    private List<String> dropdownItems;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dropdownItems = List.of(
                "Diabetes",
                "Hypertension",
                "Asthma",
                "Arthritis",
                "Depression"
        );

        multiDropdown.setItems(dropdownItems);
    }

    //Login/Register
    @FXML
    private void AppearLogin(ActionEvent event) {
        Transitions.FadeOutIn(myPane, LoginForm,650, Transitions.Direction.TO_RIGHT, 500);
    }

    @FXML
    private void AppearRegister(MouseEvent event) {
        Transitions.FadeOutIn(LoginForm, RegisterForm,650, Transitions.Direction.TO_RIGHT, 500);
    }

    @FXML
    private void AppearLoginAgain(MouseEvent event) {
        Transitions.FadeOutIn(RegisterForm, LoginForm,650, Transitions.Direction.TO_RIGHT, 500);
    }

    @FXML
    private void enterHomePage(ActionEvent event) {
        Transitions.FadeIn(home,1,Transitions.Direction.TO_LEFT,500);
        Transitions.FadeOutIn(landingPage, homePage,650, Transitions.Direction.TO_TOP, 500);
    }

    @FXML
    private void exitHomePage(ActionEvent event) {
        disSidebar();
        Transitions.FadeOut(account,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(results,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(bluetooth,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(home,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(RegisterForm,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(LoginForm,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(myPane,1,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOutIn(homePage, landingPage,650, Transitions.Direction.TO_TOP, 500);
    }



    //Sidebar
    //Is Enabled Functions

    @FXML
    private void EditAccountClick(MouseEvent event) {
        paneEditDiseases.setVisible(false);
        paneEditAccount.setVisible(true);
        Transitions.FadeOut(editDiseases,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(editAccount,1,Transitions.Direction.TO_LEFT,0);
    }

    @FXML
    private void EditDiseasesClick(MouseEvent event) {
        paneEditAccount.setVisible(false);
        paneEditDiseases.setVisible(true);
        Transitions.FadeOut(editAccount,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(editDiseases,1,Transitions.Direction.TO_LEFT,0);
    }

    @FXML
    private void HomeEnabled(MouseEvent event) {
        Images.HomeEnabled(homeImg,accountImg,resultsImg, bluetoothImg);
        Transitions.FadeOut(account,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(results,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(bluetooth,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(home,700,Transitions.Direction.TO_LEFT,500);
        disSidebar();
    }

    @FXML
    private void AccountEnabled(MouseEvent event) {
        Images.AccountEnabled(homeImg,accountImg,resultsImg, bluetoothImg);
        Transitions.FadeOut(home,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(results,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(bluetooth,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(account,700,Transitions.Direction.TO_LEFT,500);
        disSidebar();
    }

    @FXML
    private void ResultsEnabled(MouseEvent event) {
        Images.ResultsEnabled(homeImg,accountImg,resultsImg, bluetoothImg);
        Transitions.FadeOut(home,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(account,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(bluetooth,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(results,700,Transitions.Direction.TO_LEFT,500);
        disSidebar();
    }

    @FXML
    private void BluetoothEnabled(MouseEvent event) {
        Images.BluetoothEnabled(homeImg,accountImg,resultsImg, bluetoothImg);
        Transitions.FadeOut(home,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(account,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(results,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(bluetooth,700,Transitions.Direction.TO_LEFT,500);
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
        Transitions.FadeOut(sidebar,400, Transitions.Direction.TO_RIGHT, 500);
        Transitions.FadeOut(dialogBg,400, Transitions.Direction.TO_TOP, 0);
    }
}