package client.converters;

import client.model.User;
import javafx.util.StringConverter;

public class UserToStringConverter extends StringConverter<User> {
    @Override
    public String toString(User object) {
        return object.getUsername();
    }

    @Override
    public User fromString(String string) {
        return null;
    }
}
