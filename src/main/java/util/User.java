package util;

import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private String password;
    private String salt;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.salt = salt;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getSalt() {
        return this.salt;
    }
}
