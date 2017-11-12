package client.model;

public class User extends ModelBase {
    // FIELDS:

    private String mUsername = "";
    private String mPassword = "";

    public User(String username, String password) {
        mUsername = username;
        mPassword = password;
    }

    public User() {
    }

    // SETTERS & GETTERS:

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
    public boolean isNull() {
        return mUsername.isEmpty();
    }
}
