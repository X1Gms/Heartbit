package com.heartbit.heartbit_project;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

public class Validations {

    public static boolean isValidEmail(String email) {
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();  // Throws AddressException if invalid
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }


    public static boolean isValidPassword(String password) {
        if (password.length() < 12) {
            return false;
        }

        boolean hasDigit = false;
        boolean hasSpecial = false;
        String specialChars = "!@#$%^&*()_-+=[]{};':|,.<>/?\"\\";

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (specialChars.indexOf(c) >= 0) {
                hasSpecial = true;
            }
        }
        return hasDigit && hasSpecial;
    }


    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null || phone.isEmpty()) return false;

        if (!phone.startsWith("+")) return false;

        String[] parts = phone.substring(1).split("[ -]");

        // Country code length check (1-3 digits)
        if (parts.length == 0 || parts[0].length() < 1 || parts[0].length() > 3) return false;

        // Check all parts are digits only
        for (String part : parts) {
            if (!part.matches("\\d+")) {
                return false;
            }
        }

        // Optional: check minimum and maximum total length, groups count, etc.
        return true;
    }


}
