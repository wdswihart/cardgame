package client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class User extends ModelBase {
    private String mUsername = "";
    private String mPassword = "";

    public User(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public User() {
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    @Override
    @JsonIgnore
    public boolean isDefault() {
        return mUsername.isEmpty();
    }
}
