package com.heartbit.heartbit_project.db;

public class Validation {

    private final String rgxEmail;
    private final String rgxPassword;
    private final String rgxPhone;

    public Validation() {
        this.rgxEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        this.rgxPassword = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=.*[a-zA-Z]).{12,}$";
        this.rgxPhone = "^\\+(?=(?:.*\\d){7,15}$)[0-9][0-9\\- ]*\\d$";
    }

    public String getRgxEmail() {
        return rgxEmail;
    }

    public String getRgxPassword() {
        return rgxPassword;
    }

    public String getRgxPhone() {
        return rgxPhone;
    }
}
