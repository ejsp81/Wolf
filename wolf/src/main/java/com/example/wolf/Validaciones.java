package com.example.wolf;

import java.util.regex.Pattern;

public class Validaciones {




    public static  boolean validaPassword(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public static boolean validaEmail(String email) {
        boolean r = false;
        Pattern pattern = null;
        String expresion = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        r = pattern.matches(expresion, email);
        return r;
    }
}
