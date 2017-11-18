package models.responses;

import models.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlayerList implements Serializable {
    private List<Player> mPlayers = new ArrayList<>();

    public List<Player> getUsers() {
        return mPlayers;
    }
}
