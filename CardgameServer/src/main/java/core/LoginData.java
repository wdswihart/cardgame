package core;

public class LoginData extends ModelBase {
    // FIELDS:

    private String mUsername;
    private String mPassword;

    public LoginData(String username, String password) {
        mUsername = username;
        mPassword = password;
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
