package edu.duke.ece651.risc;

import java.io.Serializable;

public class Credential implements Serializable {
    private String username;
    private String password;
    Credential(String user, String pass) {
        username = user;
        password = pass;
    }
    public void set(String user, String pass) {
        username = user;
        password = pass;
    }
    public boolean check(Credential credential) {
        return username.equals(credential.username) && password.equals(credential.password);
    }
}
