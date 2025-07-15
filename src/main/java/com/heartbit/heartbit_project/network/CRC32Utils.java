package com.heartbit.heartbit_project.network;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;


public class CRC32Utils {
    public static String removeCrc32Field(String json) {
        return json.replaceAll(",?\\s*\"crc32\"\\s*:\\s*\"[^\"]*\"", "");
    }
    public static String removeMd5Field(String json) {
        return json.replaceAll(",?\\s*\"md5\"\\s*:\\s*\"[^\"]*\"", "");
    }

    public static String generateCRC32(String jsonWithoutCRC) {
        CRC32 crc = new CRC32();
        crc.update(jsonWithoutCRC.getBytes(StandardCharsets.UTF_8));
        long value = crc.getValue() & 0xFFFFFFFFL; // ← FIX: ensure 32-bit unsigned value

        // Format: lowercase hex, 8 digits, zero-padded, with "0x" prefix
        return String.format("0x%08x", value);
    }
    // Validate CRC32 in a JSON string containing a crc32 field
    public static boolean validateCRC32(String fullJsonWithCRC) {
        try {
            JSONObject jsonObject = new JSONObject(fullJsonWithCRC);

            if (!jsonObject.has("crc32")) {
                System.out.println("Missing 'crc32' field.");
                return false;
            }

            String providedCRC = jsonObject.getString("crc32");

            // Remove the crc32 field before recalculating
            jsonObject.remove("crc32");

            // Serialize back to string in a consistent way
            String dataWithoutCRC = jsonObject.toString();

            String calculatedCRC = generateCRC32(dataWithoutCRC);

            System.out.println("Provided CRC:   " + providedCRC);
            System.out.println("Calculated CRC: " + calculatedCRC);

            return providedCRC.equalsIgnoreCase(calculatedCRC);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

