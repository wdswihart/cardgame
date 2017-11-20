package models.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import models.ModelBase;
import models.Player;

import java.io.Serializable;

public class GameRequest implements Serializable {
    public enum RequestType {
        Play("Play"),
        Spectate("Spectate");

        private String mName;
        RequestType(String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }

    private String mRequestType = RequestType.Play.toString();
    private Player mFromPlayer = new Player();
    private Player mToPlayer = new Player();

    public GameRequest() {
    }

    public String getRequestType() {
        return mRequestType;
    }

    public void setRequestType(String type) {
        mRequestType = type;
    }

    public Player getFromPlayer() {
        return mFromPlayer;
    }

    public Player getToPlayer() {
        return mToPlayer;
    }

    public void setFromPlayer(Player mFromPlayer) {
        this.mFromPlayer = mFromPlayer;
    }

    public void setToPlayer(Player mToPlayer) {
        this.mToPlayer = mToPlayer;
    }

}
