package client.converters;

import models.Player;
import javafx.util.StringConverter;

public class UserToStringConverter extends StringConverter<Player> {
    @Override
    public String toString(Player object) {
        return object.getUsername();
    }

    @Override
    public Player fromString(String string) {
        return null;
    }
}
