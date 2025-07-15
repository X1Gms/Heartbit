package com.heartbit.heartbit_project.db;

import io.github.cdimascio.dotenv.Dotenv;

public class HeartEnv {

    private final Dotenv dotenv;
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPass;
    private final String googleAPI;
    private final String emergencyURL;
    private final String nonEmergencyURL;


    public HeartEnv(){
        this.dotenv = Dotenv.load();
        this.dbUrl = dotenv.get("DB_URL");
        this.dbUsername = dotenv.get("DB_USERNAME");
        this.dbPass = dotenv.get("DB_PASSWORD");
        this.googleAPI = dotenv.get("Google_API");
        this.emergencyURL = dotenv.get("EMERGENCY_URL");
        this.nonEmergencyURL = dotenv.get("NON_EMERGENCY_URL");
    }

    public String getDbUrl() {
        return dbUrl;
    }
    public String getDbUsername() {
        return dbUsername;
    }
    public String getDbPass() {
        return dbPass;
    }
    public String getGoogle_API() {
        return googleAPI;
    }
    public String getEmergencyURL() {return emergencyURL;}
    public String getNonEmergencyURL() {return nonEmergencyURL;}
}
