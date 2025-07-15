package com.heartbit.heartbit_project.network;

import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

import java.util.zip.CRC32;

import static com.heartbit.heartbit_project.network.CRC32Utils.removeCrc32Field;
import static com.heartbit.heartbit_project.network.CRC32Utils.removeMd5Field;
import static com.heartbit.heartbit_project.network.JsonHandler.calculateMd5;

public class ConnectionHandler {

    private final String broker;
    private final String IP;
    private final int PORT;
    private final String topicHeartbeat;
    private final String topicCmd;
    private final String topicOffline = "heartbit/offline";
    private final String clientId;
    private final String desKey;
    private final MessageListener listener;
    private MqttClient client;

    public ConnectionHandler(String IP, int PORT, String topicHeartbeat, String topicCmd,
                             String clientId, String desKey, MessageListener listener) {
        this.IP = IP;
        this.PORT = PORT;
        this.broker = "tcp://" + IP + ":" + PORT;
        this.topicHeartbeat = topicHeartbeat;
        this.topicCmd = topicCmd;
        this.clientId = clientId;
        this.desKey = desKey;
        this.listener = listener;
    }

    public void start() {
        try {
            client = new MqttClient(broker, clientId, null);
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
                    handleMessage(topic, message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Publisher only
                }
            });

            client.connect(options);

            // Subscribe to all needed topics
            client.subscribe(topicHeartbeat);
            client.subscribe(topicCmd);
            client.subscribe(topicOffline);

            System.out.println("Connected and subscribed to topics.");

        } catch (MqttException e) {
            System.err.println("MQTT setup failed: " + e.getMessage());
        }
    }

    private void handleMessage(String topic, MqttMessage message) {
        String rawPayload = new String(message.getPayload());

        try {
            // Placeholder for decryption/JSON handling
            // String decrypted = new DESHandler(desKey).decryptDES(rawPayload);

            if (topic.equals(topicHeartbeat)) {
                handleHeartbeat(rawPayload);
            } else if (topic.equals(topicOffline)) {
                handleOfflineResponse(rawPayload);
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    private void handleHeartbeat(String payload) {
        try {
            // 1. Decrypt
            DESHandler desHandler = new DESHandler(desKey);
            String decrypted = desHandler.decrypt(payload);

            if (decrypted == null || decrypted.isBlank()) {
                System.err.println("[Heartbeat] Failed to decrypt message.");
                return;
            }

            // 2. Parse JSON
            JsonHandler jsonData = JsonHandler.processJson(decrypted);

            if (jsonData == null) {
                System.err.println("[Heartbeat] JSON parsing failed.");
                return;
            }

            // 3. CRC32 Validation
            String expectedCrc32 = jsonData.crc32;
            if (expectedCrc32 == null) {
                System.err.println("[Heartbeat] CRC32 is missing.");
                return;
            }

            // Compute CRC32 of the original JSON string excluding crc32 field
//            JSONObject jsonObject = new JSONObject(decrypted);
//            jsonObject.remove("crc32"); // remove crc32 before calculating checksum
//            String crcPayload = jsonObject.toString();
            String crcPayload = removeCrc32Field(decrypted);
            CRC32 crc = new CRC32();
            crc.update(crcPayload.getBytes());
            String calculatedCrc = String.format("0x%08X", crc.getValue());

            if (!calculatedCrc.equalsIgnoreCase(expectedCrc32)) {
//                System.out.println(jsonObject.toString());
                System.err.printf("[Heartbeat] CRC mismatch! Expected %s but got %s%n", expectedCrc32, calculatedCrc);
                return;
            }

            // 4. Output to console
//            System.out.println("\n✅ [Heartbeat] Decrypted and Valid:\n" + jsonData.toString());

            if(payload.length() > 96){
                listener.onHeartbeat(jsonData);
            }
            // 5. Notify UI or business logic
            else listener.onCacheDetected(jsonData.getHasCache());
        } catch (Exception e) {
            System.err.println("[Heartbeat] Processing error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleOfflineResponse(String payload) {
        System.out.println("[Offline] Response received: " + payload);
        try {
            // 1. Decrypt
            DESHandler desHandler = new DESHandler(desKey);
            String decrypted = desHandler.decrypt(payload);
            System.out.println(decrypted);

            if (decrypted == null || decrypted.isBlank()) {
                System.err.println("[Offline] Failed to decrypt message.");
                return;
            }

            JSONObject obj = new JSONObject(decrypted);
            String expectedMD5 = obj.getString("md5");
            if (expectedMD5 == null) {
                System.err.println("[Offline] MD5 is missing.");
                return;
            }

            // Compute CRC32 of the original JSON string excluding crc32 field
//            JSONObject jsonObject = new JSONObject(decrypted);
//            jsonObject.remove("crc32"); // remove crc32 before calculating checksum
//            String crcPayload = jsonObject.toString();
            String payloadNoMD5 = removeMd5Field(decrypted);
            if(payloadNoMD5.startsWith("{,")){
                payloadNoMD5 = "{" + expectedMD5.substring(2);
            }
            System.out.println(payloadNoMD5);
            String payloadMD5 = calculateMd5(payloadNoMD5);
            if (!payloadMD5.equalsIgnoreCase(expectedMD5)) {
//                System.out.println(jsonObject.toString());
                System.err.printf("[Offline] CRC mismatch! Expected %s but got %s%n", payloadMD5, expectedMD5);
                return;
            }

            // 4. Output to console
//            System.out.println("\n✅ [Heartbeat] Decrypted and Valid:\n" + jsonData.toString());

            else listener.onOfflineData(JsonHandler.processOfflineData(decrypted));
        } catch (Exception e) {
            System.err.println("[Heartbeat] Processing error: " + e.getMessage());
            e.printStackTrace();
        }
        // Future handling here
    }

    private boolean shouldSendCommand(String heartbeatData) {
        // Placeholder for conditional logic
        return true;
    }

    private String prepareCommandForPayload(String heartbeatData) {
        // Placeholder for data transformation logic
        return "{\"cmd\":\"example\"}";
    }

    private void publishToCmd(String payload) {
        if (client == null || !client.isConnected()) {
            System.err.println("Cannot publish, MQTT client not connected.");
            return;
        }

        try {
            // String encrypted = new DESHandler(desKey).encryptDes(payload);
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1);
            client.publish(topicCmd, message);
            System.out.println("Published to topic " + topicCmd + ": " + payload);
        } catch (Exception e) {
            System.err.println("Failed to publish: " + e.getMessage());
        }
    }
    public void publish(String data, String topic) {
        if (client == null || !client.isConnected()) {
            System.err.println("Cannot publish, MQTT client is not connected.");
            return;
        }

        try {
            MqttMessage message = new MqttMessage(data.getBytes());
            message.setQos(1); // You can adjust QoS as needed
            client.publish(topic, message);
            System.out.printf("✅ Published to topic [%s]: %s%n", topic, data);
        } catch (MqttException e) {
            System.err.printf("❌ Failed to publish to topic [%s]: %s%n", topic, e.getMessage());
        }
    }


}
