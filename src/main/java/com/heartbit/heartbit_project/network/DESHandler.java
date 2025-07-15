package com.heartbit.heartbit_project.network;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DESHandler {
    private static final String ALGORITHM = "DES/ECB/PKCS5Padding";
    private SecretKey secretKey;

    public DESHandler(String key) {
        if (key.length() != 8) {
            throw new IllegalArgumentException("Key must be exactly 8 characters for DES.");
        }
        this.secretKey = new SecretKeySpec(key.getBytes(), "DES");
    }

    public String encrypt(String plaintext) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
            return bytesToHex(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String hexCipherText) {
        try {
            byte[] encryptedBytes = hexToBytes(hexCipherText);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            System.out.println(hexCipherText);
            e.printStackTrace();
            return null;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }

    private byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] output = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            output[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return output;
    }

//    public static void main(String[] args) {
//        DESHandler des = new DESHandler("12345678");
//
//        String original = "Hello, ESP32!";
//        String encrypted = des.encrypt(original);
//        String decrypted = des.decrypt(encrypted);
//
//        System.out.println("Original:  " + original);
//        System.out.println("Encrypted: " + encrypted);
//        System.out.println("Decrypted: " + decrypted);
//    }
}
