package com.heartbit.heartbit_project;

import com.heartbit.heartbit_project.components.MultiDropdown;
import com.heartbit.heartbit_project.pages.landingPage.Register;
import com.heartbit.heartbit_project.visual_functions.Images;
import com.heartbit.heartbit_project.visual_functions.Transitions;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class HelloController implements Initializable {

    //-+-+-+-+-+- Landing Page Variables -+-+-+-+-+-
    @FXML
    private TextField loginEmail;
    @FXML
    private PasswordField loginPassword;
    @FXML
    private TextField registerName;
    @FXML
    private TextField registerEmail;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private PasswordField registerCPassword;
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

    //-+-+-+-+-+- HomePage Variables -+-+-+-+-+-
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

    //-+-+-+-+-+- Account Variables -+-+-+-+-+-
    @FXML
    private FlowPane editAccount;
    @FXML
    private VBox editDiseases;
    @FXML
    private Pane paneEditDiseases;
    @FXML
    private Pane paneEditAccount;
    @FXML
    private VBox ed_add_textfields;
    //-+-+-+-+-+- Edit Account Variables -+-+-+-+-+-

    @FXML
    private TextField edName;
    @FXML
    private TextField edEmail;
    @FXML
    private TextField edPhN;
    @FXML
    private TextField edEPhN;
    @FXML
    private TextField edPassword;
    @FXML
    private TextField edCPassword;
    //-+-+-+-+-+- Edit Account Diseases -+-+-+-+-+-
    @FXML
    private MultiDropdown multiDropdown;

    //-+-+-+-+-+- Toasts -+-+-+-+-+-

    @FXML
    private HBox successToast;
    @FXML
    private Label successMessage;
    @FXML
    private HBox errorToast;
    @FXML
    private Label textError;

    @FXML
    private void createAccount(ActionEvent event){
        String name = registerName.getText().trim();
        String email = registerEmail.getText().trim();
        String password = registerPassword.getText().trim();
        String confirmPassword = registerCPassword.getText().trim();
        Register registerData = new Register(name, email, password, confirmPassword);
        String message = registerData.validateRegisterForm();
        if (!message.isEmpty()) {
            textError.setText(message);
            Transitions.FadeIn(errorToast,350, Transitions.Direction.TO_LEFT, 500);
        }
        else{
            message = registerData.insertDataRegister();
            if (!message.isEmpty()) {
                textError.setText(message);
                Transitions.FadeIn(errorToast,350, Transitions.Direction.TO_LEFT, 500);
            } else {
                Transitions.FadeIn(home,1,Transitions.Direction.TO_LEFT,500);
                Transitions.FadeOutIn(landingPage, homePage,650, Transitions.Direction.TO_TOP, 500);
            }
        }
    }

    @FXML
    private LineChart<String, Number> lineChart; // Tem de coincidir com o fx:id do FXML

    private XYChart.Series<String, Number> bpmSeries;
    private int bpmCount = 0; // Para controlar o número da leitura

    @FXML
    private VBox bpmBox; // VBox com a borda (a que tem o styleClass "circle")

    @FXML
    private Label stateLabel; // Label que mostra o texto tipo "Stable", "Warning", "Danger"

    @FXML
    private Label bpmLabel; // Label que mostra o número BPM

    @FXML
    private VBox bpmBox2; // VBox com a borda (a que tem o styleClass "circle")

    @FXML
    private Label stateLabel2; // Label que mostra o texto tipo "Stable", "Warning", "Danger"

    @FXML
    private Label bpmLabel2; // Label que mostra o número BPM

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> dropdownItems = List.of(
                "Diabetes",
                "Hypertension",
                "Asthma",
                "Arthritis",
                "Depression"
        );

        multiDropdown.setItems(dropdownItems);

        bpmSeries = new XYChart.Series<>();
        bpmSeries.setName("BPM");
        lineChart.getData().add(bpmSeries);
        lineChart.setLegendVisible(false);


    }
    @FXML
    private void testeBPM(ActionEvent event) {
            Timeline loop = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                int bpm = 40 + (int)(Math.random() * 91); // Gera entre 40 e 130
                updateChart(bpm);
                updateBPMDisplay(bpm);
            }));
            loop.setCycleCount(Timeline.INDEFINITE); // Loop infinito
            loop.play(); // Inicia
        }

    public void updateBPMDisplay(int bpm) {
        bpmLabel.setText(String.valueOf(bpm));
        bpmLabel2.setText(String.valueOf(bpm));

        String borderColor;
        String stateText;
        String textColor;

        if (bpm >= 60 && bpm <= 100) {
            // Verde – Estável
            borderColor = "#B3FFB9";
            textColor = "#84DF8B";
            stateText = "Stable";
        } else if ((bpm >= 41 && bpm <= 59) || (bpm >= 101 && bpm <= 129)) {
            // Amarelo – Aviso
            borderColor = "#FFE5BA";
            textColor = "#E1AD4C";
            stateText = "Potencial Risk";
        } else {
            // Vermelho – Perigo
            borderColor = "#FE9F9F";
            textColor = "#E25C5C";
            stateText = "Critical";
        }

        bpmBox.setStyle("-fx-border-color: " + borderColor + ";");
        stateLabel.setText(stateText);
        stateLabel.setStyle("-fx-text-fill: " + textColor + ";");

        bpmBox2.setStyle("-fx-border-color: " + borderColor + ";");
        stateLabel2.setText(stateText);
        stateLabel2.setStyle("-fx-text-fill: " + textColor + ";");
    }

    public void updateChart(int bpm) {
        bpmSeries.getData().add(new XYChart.Data<>("L" + bpmCount, bpm));
        bpmCount++;

        if (bpmSeries.getData().size() > 4) {
            PauseTransition pause = new PauseTransition(Duration.millis(100));
            pause.setOnFinished(e -> bpmSeries.getData().remove(0));
            pause.play();
        }
        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();

        // Valores visíveis
        List<Number> valores = bpmSeries.getData().stream()
                .map(XYChart.Data::getYValue)
                .collect(Collectors.toList());

        double max = valores.stream().mapToDouble(Number::doubleValue).max().orElse(110);

        // LowerBound fixo em 0
        yAxis.setLowerBound(0);

        // UpperBound dinâmico
        if (max <= 110) {
            yAxis.setUpperBound(110);
        } else {
            // Arredonda para o múltiplo de 10 acima
            double roundedMax = Math.ceil(max / 10.0) * 10;
            yAxis.setUpperBound(roundedMax);
        }

        yAxis.setAutoRanging(false); // garante que o gráfico respeita os limites definidos

    }


    @FXML
    private void AddTextField(ActionEvent event) {
        // Criar o Label
        Label label = new Label("Other Disease");
        label.setPrefWidth(240.0);
        label.getStyleClass().add("form-login-input");
        VBox.setMargin(label, new Insets(30, 0, 0, 15));

        // Criar o TextField
        TextField textField = new TextField();
        textField.setPrefHeight(47.0);
        textField.setPrefWidth(268.0);
        textField.getStyleClass().add("text-field");

        // Botão para remover
        Button removeBtn = new Button("-");
        removeBtn.setPrefSize(52, 52);
        removeBtn.setMinWidth(52);
        removeBtn.setMinHeight(52);
        removeBtn.getStyleClass().add("home-btn");
        removeBtn.setTextFill(Color.WHITE);
        removeBtn.setFont(Font.font("Tahoma", 20));

        // HBox contêiner
        HBox hbox = new HBox(10, new VBox(label, textField), removeBtn);
        hbox.setAlignment(Pos.BOTTOM_LEFT);
        hbox.setPrefSize(200,100);

        // Associar ação de remover
        removeBtn.setOnAction(e -> {
            VBox parentVBox = (VBox) hbox.getParent();
            parentVBox.getChildren().remove(hbox);
        });

        // Adicionar ao container principal
        ed_add_textfields.getChildren().add(hbox);
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
        Transitions.FadeOut(home,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(RegisterForm,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(LoginForm,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(myPane,1,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOutIn(homePage, landingPage,650, Transitions.Direction.TO_TOP, 500);
        Images.HomeEnabled(homeImg,accountImg,resultsImg);
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
        Images.HomeEnabled(homeImg,accountImg,resultsImg);
        Transitions.FadeOut(account,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(results,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(home,700,Transitions.Direction.TO_LEFT,500);
        disSidebar();
    }

    @FXML
    private void AccountEnabled(MouseEvent event) {
        Images.AccountEnabled(homeImg,accountImg,resultsImg);
        Transitions.FadeOut(home,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(results,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(account,700,Transitions.Direction.TO_LEFT,500);
        disSidebar();
    }

    @FXML
    private void ResultsEnabled(MouseEvent event) {
        Images.ResultsEnabled(homeImg,accountImg,resultsImg);
        Transitions.FadeOut(home,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(account,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(results,700,Transitions.Direction.TO_LEFT,500);
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

    @FXML
    private void closeError(MouseEvent event) {
        Transitions.FadeOut(errorToast,400, Transitions.Direction.TO_LEFT, 500);
    }

    @FXML
    private void closeSuccess(MouseEvent event) {
        Transitions.FadeOut(successToast,400, Transitions.Direction.TO_LEFT, 500);
    }
}