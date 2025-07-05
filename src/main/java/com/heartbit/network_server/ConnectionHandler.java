package com.heartbit.network_server;

import org.eclipse.paho.client.mqttv3.*;

public class ConnectionHandler {

    private final String broker;
    private final String IP;
    private final int PORT;
    private final String topic;
    private final String clientId;
    private final String desKey;
    private final MessageListener listener;

    public ConnectionHandler(String IP, int PORT, String topic, String clientId, String desKey, MessageListener listener) {
        this.IP = IP;
        this.PORT = PORT;
        this.broker = "tcp://" + IP + ":" + PORT;
        this.topic = topic;
        this.clientId = clientId;
        this.desKey = desKey;
        this.listener = listener;
    }

    public void start() {
        try {
            MqttClient client = new MqttClient(broker, clientId,null);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);

            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("MQTT connection lost: " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    try {
                        String encryptedHex = new String(message.getPayload());
                        String decryptedJson = DESHandler.decryptDES(encryptedHex, desKey);
                        JsonHandler data = JsonHandler.processJson(decryptedJson);
                        if (data != null && listener != null) {
                            listener.onMessage(data);
                        }
                    } catch (Exception e) {
                        System.err.println("Decryption failed: " + e.getMessage());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Not needed for subscriber
                }
            });

            client.connect(options);
            System.out.println("Connected to MQTT broker.");
            client.subscribe(topic);
            System.out.println("Subscribed to topic: " + topic);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public String getIP() {
        return IP;
    }

    public int getPORT() {
        return PORT;
    }
}
