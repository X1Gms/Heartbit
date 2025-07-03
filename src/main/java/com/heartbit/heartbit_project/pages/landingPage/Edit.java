package com.heartbit.heartbit_project.pages.landingPage;

public class Edit {
    public static void edit(String editName, String editEmail, String editPassword, String editPasswordConfirm, String editNumTelephone, String editNumEmergency) {
        //Validações

        editName = editName.trim();
        editEmail = editEmail.trim();
        editPassword = editPassword.trim();
        editPasswordConfirm = editPasswordConfirm.trim();
        editNumTelephone = editNumTelephone.trim();
        editNumEmergency = editNumEmergency.trim();

        if(editName.isEmpty()) {
            System.out.println("Name field is empty. Insert a name.");
        }
        else if(!editEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$")) {
            System.out.println("Incorrect email address. Insert a valid email.");
        }
        else if(!(editPassword.length() < 12) && editPassword.contains("^[0-9._%+-]")) {
            System.out.println("Password is not valid. Insert a valid password.");
        }
        else if(!editPassword.matches(editPasswordConfirm)) {
            System.out.println("Password confirmation doesn't match Password.");
        }
        else if(editNumTelephone.matches(editNumEmergency)) {
            System.out.println("Telephone number cannot be the same as emergency number. Insert another number.");
        }
        else if(!(editNumTelephone.matches("^\\+?[0-9]{1,3}?[-.\\s]?(?:\\(?[0-9]{1,4}\\)?[-.\\s]?)[0-9]{1,4}-[0-9]{1,9}$")) && editNumTelephone.matches("^\\+?[0-9]{1,3}?[-.\\s]?(?:\\(?[0-9]{1,4}\\)?[-.\\s]?)[0-9]{1,4}-[0-9]{1,9}$")) {
            System.out.println("Invalid telephone number. Insert a valid telephone number.");
        }
    }
}
