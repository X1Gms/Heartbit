package com.heartbit.heartbit_project;

import com.heartbit.heartbit_project.components.MultiDropdown;
import com.heartbit.heartbit_project.db.*;
import com.heartbit.heartbit_project.network.*;
import com.heartbit.heartbit_project.network.JsonHandler;
import com.heartbit.heartbit_project.network.PDFGenerator;
import com.heartbit.heartbit_project.service.ReverseGeocoder;
import com.heartbit.heartbit_project.visual_functions.Images;
import com.heartbit.heartbit_project.visual_functions.Transitions;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.text.TextAlignment;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.UUID;

public class HeartBitController implements Initializable {

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
    private TextField phN;
    @FXML
    private TextField ePhN;
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
    @FXML
    private Label nameField;

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
    @FXML
    private TextField otherDisease;

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
    private User user;
    @FXML
    private Disease disease;
    EmergencyLog log;
    @FXML
    private JsonHandler heartBitJsonHandler;

    private HeartEnv heartEnv = new HeartEnv();

    private ArrayList<Integer> bpmOffline = new ArrayList<Integer>();

    // MQTT Integration
    private ConnectionHandler connectionHandler;
    private final AtomicBoolean jaProcessou = new AtomicBoolean(false);
    private ReverseGeocoder geocoder = new ReverseGeocoder();
    private final String nonEmergencyURL = heartEnv.getNonEmergencyURL();
    private final String emergencyURL = heartEnv.getEmergencyURL();
    private final String MQTT_TOPIC = "heartbit/bpm";
    private final String CMD_TOPIC = "heartbit/cmd";
    private final String CLIENT_ID = UUID.randomUUID().toString();
    private final String DES_KEY = "12345678";
    private ArrayList<Integer> bpms = new ArrayList<>();

    private final ArrayList<String> dropdownItems = new ArrayList<>(
            Arrays.asList(
                    "Coronary Artery",
                    "Previous Infarction",
                    "Heart Failure",
                    "Tachycardia",
                    "Fibrillation",
                    "Bradycardia",
                    "Heart Block",
                    "Hypertension",
                    "Hyperlipidemia",
                    "Valvular Heart ",
                    "Dilated Cardiomyopathy",
                    "Congenital Heart ",
                    "Pericarditis",
                    "Cardiac Surgery"
            )
    );


    @FXML
    private LineChart<String, Number> lineChart; // Tem de coincidir com o fx:id do FXML

    private XYChart.Series<String, Number> bpmSeries;
    private int bpmCount = 0; // Para controlar o número da leitura

    @FXML
    private VBox bpmBox; // VBox com a borda (a que tem o styleClass "circle")

    @FXML
    private Label stateLabel; // Label que mostra o texto tipo "Stable", "Potencial Risk", "Critical"

    @FXML
    private Label bpmLabel; // Label que mostra o número BPM

    @FXML
    private VBox bpmBox2; // VBox com a borda (a que tem o styleClass "circle")

    @FXML
    private Label stateLabel2; // Label que mostra o texto tipo "Stable", "Potencial Risk", "Critical"

    @FXML
    private Label bpmLabel2; // Label que mostra o número BPM

    @FXML
    private VBox phoneForm;

    @FXML private Label minLabel;
    @FXML private Label maxLabel;
    @FXML private Label avgLabel;

    @FXML private Pane minPane;
    @FXML private Pane maxPane;
    @FXML private Pane avgPane;

    @FXML
    private VBox eventList;

    @FXML
    private Button myButton2;

    private final List<Integer> bpmHistory = new ArrayList<>();

    private record EventData(int bpm, String location, Timestamp ts) {}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        multiDropdown.setItems(dropdownItems);
        bpmSeries = new XYChart.Series<>();
        bpmSeries.setName("BPM");
        lineChart.getData().add(bpmSeries);
        lineChart.setLegendVisible(false);
    }

    // Initialize MQTT Connection
    private void setupMQTT() {
        MessageListener listener = new MessageListener() {
            @Override
            public void onHeartbeat(JsonHandler data) {
                Platform.runLater(() -> {
                    System.out.println("\n📥 Heartbeat:\n" + data.toString() + "\n");
                    bpms.add(data.getBpm());

                    // Store BPM in database
                    Bpm bpmInst = new Bpm(user, data.getBpm());
                    int bpmId = bpmInst.insertBpmData();
                    bpmInst.setId(bpmId);

                    updateChart(data.getBpm());
                    updateBPMDisplay(bpmInst);

                    try {
                        updateBPMStats();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    if (bpms.size() >= 5) {
                        bpms.clear();
                    }

                    // Handle emergency status
                    if (data.getStatus().equalsIgnoreCase("4")) {
                        if (jaProcessou.compareAndSet(false, true)) {
                            String location = geocoder.lookup(data.getLat(), data.getLon()).address();
                            String payload = """
                                {
                                  "embeds": [
                                    {
                                      "author": {
                                        "name": "HeartBit",
                                        "icon_url": "https://cdn.discordapp.com/attachments/1392568327724994682/1394023438217252924/ChatGPT_Image_16_06_2025_16_16_18_1-removebg-preview.png?ex=68754cff&is=6873fb7f&hm=7700a926c921f33f48a977b5322878f5b30cd6ba3590c9d1719053a571dfaff8&"
                                      },
                                      "description": "The hearbit system detected a POSSIBLE HEART FAILURE.\\nThis is a **Emergency**, please send medical help to: """
                                    + location +
                                    """
                                                  .\\nHearbit System's will always monitor its users.",
                                                  "title": "Heartbit - Detected Emergency",
                                                  "color": 15158332
                                                }
                                              ]
                                            }
                                            """;

                            sendWebhook(emergencyURL, payload);

                            // Create emergency log
                            EmergencyLog emergencyLog = new EmergencyLog(bpmInst, 0,
                                    String.valueOf(data.getLat()),
                                    String.valueOf(data.getLon()));
                            emergencyLog.insertEmergencyData();
                        }
                    }
                    else if (data.getStatus().equalsIgnoreCase("1")) {
                        if (jaProcessou.compareAndSet(false, true)) {
                            String payload = """
                                {
                                  "embeds": [
                                    {
                                      "author": {
                                        "name": "HeartBit",
                                        "icon_url": "https://cdn.discordapp.com/attachments/1392568327724994682/1394023438217252924/ChatGPT_Image_16_06_2025_16_16_18_1-removebg-preview.png?ex=68754cff&is=6873fb7f&hm=7700a926c921f33f48a977b5322878f5b30cd6ba3590c9d1719053a571dfaff8&"
                                      },
                                      "description": "The hearbit system detected an occurenced, which was canceled by the user.\\nThis is a **Non Emergency**, but we recommend to contact the user.\\nHearbit System's will always monitor its users.",
                                      "title": "Heartbit - Canceled Emergency",
                                      "color": 15859456
                                    }
                                  ]
                                }
                                """;
                            sendWebhook(nonEmergencyURL, payload);
                        }
                    }
                });
            }

            @Override
            public void onCacheDetected(Boolean hasCache) {
                Platform.runLater(() -> {
                    System.out.println("Has Cache: " + hasCache);
                    myButton2.setVisible(hasCache);
                });
            }

            // Supondo que bpmOffline é um campo da classe:
            private final List<Integer> bpmOffline = new ArrayList<>();

            @Override
            public void onOfflineData(List<DataPoint> dataPoints) {
                for (DataPoint d : dataPoints) {
                    bpmOffline.add(d.bpm());

                    if (bpmOffline.size() >= 5) {
                        enviarMediaParaBD();
                        bpmOffline.clear();
                    }
                }

                if (!bpmOffline.isEmpty()) {
                    enviarMediaParaBD();
                    bpmOffline.clear();
                }
            }

            private void enviarMediaParaBD() {
                int soma = 0;
                for (int bpm : bpmOffline) {
                    soma += bpm;
                }
                int media = soma / bpmOffline.size();

                Bpm bpmInst = new Bpm(user, media);
                bpmInst.insertBpmData();
            }

        };

        connectionHandler = new ConnectionHandler(
                "localhost",
                1883,
                MQTT_TOPIC,
                CMD_TOPIC,
                CLIENT_ID,
                DES_KEY,
                listener
        );
        connectionHandler.start();
    }

    private void sendWebhook(String urlString, String payload) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Webhook sent. Response code: " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateDiseases(ArrayList<String> dbList) {
        ed_add_textfields.getChildren().clear();
        if (dbList == null) dbList = new ArrayList<>();
        ArrayList<String> knownMatches = new ArrayList<>();
        for (String d : dbList) {
            if (dropdownItems.contains(d)) {
                knownMatches.add(d);
            }
        }
        multiDropdown.setSelectedItems(knownMatches);

        boolean firstOther = true;
        for (String d : dbList) {
            if (!dropdownItems.contains(d)) {
                if (firstOther) {
                    otherDisease.setText(d);
                    firstOther = false;
                } else {
                    AddOtherDisease(d);
                }
            }
        }
    }

    @FXML
    private void submitDiseases(ActionEvent event) {
        ArrayList<String> diseaseInputs = new ArrayList<>();
        boolean hasCustomFields = false;
        boolean hasEmptyField = false;

        for (Node node : ed_add_textfields.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;

                if (!hbox.getChildren().isEmpty()) {
                    Node inner = hbox.getChildren().get(0);
                    if (inner instanceof VBox) {
                        VBox vbox = (VBox) inner;

                        for (Node child : vbox.getChildren()) {
                            if (child instanceof TextField) {
                                hasCustomFields = true;
                                String text = ((TextField) child).getText();
                                if (text != null && !text.trim().isEmpty()) {
                                    diseaseInputs.add(text.trim());
                                } else {
                                    hasEmptyField = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (hasCustomFields) {
            String otherDiseaseText = otherDisease.getText().trim();
            if (!otherDiseaseText.isEmpty()) {
                diseaseInputs.add(otherDiseaseText.trim());
            } else {
                textError.setText("Empty Text Box");
                Transitions.FadeIn(errorToast, 350, Transitions.Direction.TO_LEFT, 500);
                return;
            }
        }

        if (hasEmptyField) {
            textError.setText("Empty Text Box\nDelete it to continue.");
            Transitions.FadeIn(errorToast, 350, Transitions.Direction.TO_LEFT, 500);
            return;
        }

        else {
            String otherDiseaseText = otherDisease.getText().trim();
            if (!otherDiseaseText.trim().isEmpty()) {
                diseaseInputs.add(otherDiseaseText.trim());
            }
        }

        diseaseInputs.addAll(multiDropdown.getValues());
        disease = new Disease(diseaseInputs, user);

        String message = disease.syncUserDiseases();
        if (!message.isEmpty()) {
            textError.setText(message);
            Transitions.FadeIn(errorToast,350, Transitions.Direction.TO_LEFT, 500);
        } else {
            enHome();
            successMessage.setText("Diseases successfully edited");
            multiDropdown.setSelectedItems(new ArrayList<>());
            ed_add_textfields.getChildren().clear();
            otherDisease.setText("");
            Transitions.FadeIn(successToast,350, Transitions.Direction.TO_LEFT, 500);
        }
    }

    @FXML
    private void createAccount(ActionEvent event) throws SQLException, ClassNotFoundException {
        String phoneNumber = phN.getText().trim();
        String ePhoneNumber = ePhN.getText().trim();
        user.setPhoneNumber(phoneNumber);
        user.setEmergencyPhoneNumber(ePhoneNumber);
        String message = user.validatePhoneForm();
        if (!message.isEmpty()) {
            textError.setText(message);
            Transitions.FadeIn(errorToast,350, Transitions.Direction.TO_LEFT, 500);
        }
        else{
            message = user.insertDataRegister();
            if (!message.isEmpty()) {
                textError.setText(message);
                Transitions.FadeIn(errorToast,350, Transitions.Direction.TO_LEFT, 500);
            } else {
                user = user.getUserDetails();
                if (!user.getName().isEmpty()){
                    nameField.setText(user.getName());

                }
                registerEmail.setText("");
                registerCPassword.setText("");
                registerName.setText("");
                phN.setText("");
                ePhN.setText("");
                disease = new Disease(user);
                disease.setDiseases(disease.getUserDiseases());
                Transitions.FadeIn(home,1,Transitions.Direction.TO_LEFT,500);
                Transitions.FadeOutIn(landingPage, homePage,650, Transitions.Direction.TO_TOP, 500);
                setupMQTT(); // Start MQTT after successful registration
                updateBPMStats();
                retrieveEvents();
            }
        }
    }

    @FXML
    private void editAccount(ActionEvent event){
        String name = edName.getText().trim();
        String email = edEmail.getText().trim();
        String password = edPassword.getText() == null ? "" : edPassword.getText().trim();
        String cPassword = edCPassword.getText().trim();
        String phN = edPhN.getText().trim();
        String ePhN = edEPhN.getText().trim();

        User tempUser = user;
        tempUser.setName(name);
        tempUser.setEmail(email);
        tempUser.setPassword(password);
        tempUser.setPasswordConfirm(cPassword);
        tempUser.setPhoneNumber(phN);
        tempUser.setEmergencyPhoneNumber(ePhN);
        String message = tempUser.editProfile();

        if (!message.isEmpty()) {
            textError.setText(message);
            Transitions.FadeIn(errorToast,350, Transitions.Direction.TO_LEFT, 500);
        } else {
            user = user.getUserDetails();
            if (!user.getName().isEmpty()){
                nameField.setText(user.getName());
                enHome();
                successMessage.setText("User successfully edited");
                Transitions.FadeIn(successToast,350, Transitions.Direction.TO_LEFT, 500);
            }

        }
    }

    @FXML
    private void exportPDF(MouseEvent event) throws SQLException {
        Bpm bpmCollection = new Bpm();
        bpmCollection.getBpmStatus(user.getId());
        PDFGenerator pdfGenerator = new PDFGenerator(user, disease, log.getEmergencyReadings(), bpmCollection);
        String userName = user.getName(); // e.g., "John"
        String date = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String fileName = "myReport" + userName + date + ".pdf";
        pdfGenerator.promptAndGeneratePDF(fileName);
    }

    private void AddOtherDisease(String value){
        // Criar o Label
        Label label = new Label("Other Disease");
        label.setPrefWidth(240.0);
        label.getStyleClass().add("form-login-input");
        VBox.setMargin(label, new Insets(30, 0, 0, 15));

        // Criar o TextField
        TextField textField = new TextField(value);
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
    @FXML
    private void AddTextField(ActionEvent event) {
        AddOtherDisease("");
    }

    @FXML
    private void handleButtonClick() {
        if (myButton2.isVisible()) {
            String cmd = JsonHandler.generateCommandPrompt("send_offline_data");
            DESHandler desH = new DESHandler("12345678");
            cmd = desH.encrypt(cmd);
            connectionHandler.publish(cmd, CMD_TOPIC);
            myButton2.setVisible(false);
        }
    }

    public void updateBPMDisplay(Bpm bpm) {
        bpmLabel.setText(String.valueOf(bpm.getValue()));
        bpmLabel2.setText(String.valueOf(bpm.getValue()));

        String borderColor;
        String stateText;
        String textColor;

        if (bpm.getValue() >= 60 && bpm.getValue() <= 100) {
            // Verde – Estável
            borderColor = "#B3FFB9";
            textColor = "#84DF8B";
            stateText = "Stable";
        } else {
            if ((bpm.getValue() >= 41 && bpm.getValue() <= 59) || (bpm.getValue() >= 101 && bpm.getValue() <= 129)) {
                // Amarelo – Aviso
                borderColor = "#FFE5BA";
                textColor = "#E1AD4C";
                stateText = "Potential Risk";
            } else {
                // Vermelho – Perigo
                borderColor = "#FE9F9F";
                textColor = "#E25C5C";
                stateText = "Critical";
            }
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
            pause.setOnFinished(e -> bpmSeries.getData().removeFirst());
            pause.play();
        }
        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();

        // Valores visíveis
        List<Number> valores = bpmSeries.getData().stream()
                .map(XYChart.Data::getYValue)
                .toList();

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

    private void updateBPMStats() throws SQLException {
        if (user == null)
            return;

        Bpm bpmCollection = new Bpm();
        bpmCollection.getBpmStatus(user.getId());

        int min = bpmCollection.getMinBPM();
        int max = bpmCollection.getMaxBPM();
        int avg = bpmCollection.getAvgBPM();

        minLabel.setText(String.valueOf(min));
        maxLabel.setText(String.valueOf(max));
        avgLabel.setText(String.valueOf(avg));

        minPane.setStyle("-fx-background-color: " + getColorForBPM(min) + ";");
        maxPane.setStyle("-fx-background-color: " + getColorForBPM(max) + ";");
        avgPane.setStyle("-fx-background-color: " + getColorForBPM(avg) + ";");
    }

    private String getColorForBPM(int bpm) {
        if (bpm >= 60 && bpm <= 100) {
            return "#B3FFB9"; // Verde
        } else if ((bpm >= 41 && bpm <= 59) || (bpm >= 101 && bpm <= 129)) {
            return "#FFE5BA"; // Amarelo
        } else {
            return "#FE9F9F"; // Vermelho
        }
    }

    private void retrieveEvents() {
        Task<List<EventData>> task = new Task<>() {
            @Override
            protected List<EventData> call() throws Exception {

                if (user == null) {
                    return List.of();       // nothing to do
                }

                log = new EmergencyLog();
                log.getEmergencyReadingsForUser(user.getId());

                ReverseGeocoder geocoder = new ReverseGeocoder();
                List<EventData> out = new ArrayList<>();

                for (EmergencyReading r : log.getEmergencyReadings()) {
                    var loc = geocoder.lookup(
                            Double.parseDouble(r.getLat()),
                            Double.parseDouble(r.getLon()));

                    out.add(new EventData(r.getBpmValue(), loc.city(), r.getDateTime()));
                }
                return out;                 // = value of task
            }
        };
        task.setOnSucceeded(e -> {
            eventList.getChildren().clear(); // Clear existing events
            task.getValue().forEach(ev ->
                    addEvent(ev.bpm(), ev.location(), ev.ts()));
        });

        task.setOnFailed(e -> {
            task.getException().printStackTrace();
        });

        Thread t = new Thread(task, "retrieve-events");
        t.setDaemon(true);
        t.start();
    }

    private void addEvent(int bpm, String location, Timestamp timestamp) {
        String status;
        String color;

        if ((bpm >= 41 && bpm <= 59) || (bpm >= 101 && bpm <= 129)) {
            status = "Potencial Risk";
            color = "#FFF3F3";
        } else if (bpm <= 40 || bpm >= 130) {
            status = "Critical";
            color = "#FFBDBD";
        } else {
            return;
        }

        HBox row = new HBox();
        row.setStyle("-fx-background-color: " + color + "; -fx-padding: 10 15;");
        row.setAlignment(Pos.CENTER_LEFT);
        row.setSpacing(40);
        row.setPrefHeight(60);
        row.setMinHeight(60);
        row.setMaxHeight(60);

        Label statusLabel = new Label(status);
        statusLabel.setMinWidth(90);
        statusLabel.setPrefWidth(90);
        statusLabel.setMaxWidth(90);
        statusLabel.setAlignment(Pos.CENTER);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-family: Tahoma;");

        Label bpmLabel = new Label(String.valueOf(bpm));
        bpmLabel.setMinWidth(60);
        bpmLabel.setPrefWidth(60);
        bpmLabel.setMaxWidth(60);
        bpmLabel.setAlignment(Pos.CENTER);
        bpmLabel.setStyle("-fx-font-size: 16px; -fx-font-family: Tahoma;");

        Label locationLabel = new Label(location);
        locationLabel.setMinWidth(90);
        locationLabel.setPrefWidth(90);
        locationLabel.setMaxWidth(90);
        locationLabel.setWrapText(true);
        locationLabel.setAlignment(Pos.CENTER);
        locationLabel.setStyle("-fx-font-size: 14px; -fx-font-family: Tahoma;");

        // Convert Timestamp to LocalDateTime
        LocalDateTime dateTime = timestamp.toLocalDateTime();

        String dateStr = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String timeStr = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        Label timeLabel = new Label(dateStr + "\n" + timeStr);
        timeLabel.setMinWidth(90);
        timeLabel.setPrefWidth(90);
        timeLabel.setMaxWidth(90);
        timeLabel.setAlignment(Pos.BASELINE_RIGHT);
        timeLabel.setTextAlignment(TextAlignment.CENTER);
        timeLabel.setWrapText(true);
        timeLabel.setStyle("-fx-font-size: 14px; -fx-font-family: Tahoma;");

        row.getChildren().addAll(statusLabel, bpmLabel, locationLabel, timeLabel);

        eventList.getChildren().addFirst(row);
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
    private void MoveToPhoneForm(ActionEvent event) {
        String name = registerName.getText().trim();
        String email = registerEmail.getText().trim();
        String password = registerPassword.getText().trim();
        String confirmPassword = registerCPassword.getText().trim();
        user = new User(name, email, password);
        user.setPasswordConfirm(confirmPassword);
        String message = user.validateRegisterForm();
        if (!message.isEmpty()) {
            textError.setText(message);
            Transitions.FadeIn(errorToast,350, Transitions.Direction.TO_LEFT, 500);
        }
        else{
            Transitions.FadeOutIn(RegisterForm, phoneForm,650, Transitions.Direction.TO_RIGHT, 500);
        }

    }

    @FXML
    private void AppearLoginAgain(MouseEvent event) {
        Transitions.FadeOutIn(RegisterForm, LoginForm,650, Transitions.Direction.TO_RIGHT, 500);
    }

    @FXML
    private void enterHomePage(ActionEvent event) throws SQLException, ClassNotFoundException {
        String email = loginEmail.getText().trim();
        String password = loginPassword.getText().trim();
        user = new User(email, password);
        String message = user.Login();
        if (!message.isEmpty()) {
            textError.setText(message);
            Transitions.FadeIn(errorToast,350, Transitions.Direction.TO_LEFT, 500);
        }
        else{
            user = user.getUserDetails();
            if (!user.getName().isEmpty()){
                nameField.setText(user.getName());
            }
            loginEmail.setText("");
            loginPassword.setText("");
            disease = new Disease(user);
            disease.setDiseases(disease.getUserDiseases());
            Transitions.FadeIn(home,1,Transitions.Direction.TO_LEFT,500);
            Transitions.FadeOutIn(landingPage, homePage,650, Transitions.Direction.TO_TOP, 500);
            setupMQTT(); // Start MQTT after successful login
            updateBPMStats();
            retrieveEvents();
        }

    }

    @FXML
    private void exitHomePage(ActionEvent event) {
        if (connectionHandler != null) {
            connectionHandler = null;
        }
        user = null;
        disease = null;
        log = null;
        disSidebar();
        Transitions.FadeOut(account,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(results,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(home,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(RegisterForm,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(phoneForm,0,Transitions.Direction.TO_LEFT,0);
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

    private void enHome(){
        Images.HomeEnabled(homeImg,accountImg,resultsImg);
        Transitions.FadeOut(account,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(results,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(home,700,Transitions.Direction.TO_LEFT,500);
        disSidebar();
    }

    @FXML
    private void HomeEnabled(MouseEvent event) {
        enHome();
    }

    @FXML
    private void AccountEnabled(MouseEvent event) throws SQLException, ClassNotFoundException {
        Images.AccountEnabled(homeImg,accountImg,resultsImg);
        Transitions.FadeOut(home,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(results,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(account,700,Transitions.Direction.TO_LEFT,500);
        user = user.getUserDetails();
        disease = new Disease(user);
        disease.setDiseases(disease.getUserDiseases());
        List<String> fetched = disease.getDiseases();            // might be null
        ArrayList<String> dbList = new ArrayList<>();
        if (fetched != null) {
            dbList.addAll(fetched);
        }
        populateDiseases(dbList);
        if (user != null){
            edName.setText(user.getName());
            edEmail.setText(user.getEmail());
            edPassword.setText(user.getPassword());
            edPhN.setText(user.getPhoneNumber());
            edEPhN.setText(user.getEmergencyPhoneNumber());
            edCPassword.setText("");
        }
        disSidebar();
    }

    @FXML
    private void ResultsEnabled(MouseEvent event) {
        Images.ResultsEnabled(homeImg,accountImg,resultsImg);
        Transitions.FadeOut(home,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeOut(account,0,Transitions.Direction.TO_LEFT,0);
        Transitions.FadeIn(results,700,Transitions.Direction.TO_LEFT,500);
        disSidebar();
        retrieveEvents(); // Refresh events when results page is opened
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