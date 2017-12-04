package models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Player extends ModelBase {
    private String mUsername = "";
    private String mPassword = "";

    public Player(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public Player() {
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

    public boolean equals(Player player) {
        return player.getUsername().equals(mUsername);
    }
}
