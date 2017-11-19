package models.requests;

import models.Player;

public class GameRequest {
    private RequestType mRequestType = RequestType.Play;
    private Player mFromPlayer;
    private Player mToPlayer;

    public RequestType getRequestType() {
        return mRequestType;
    }

    public Player getFromPlayer() {
        return mFromPlayer;
    }

    public Player getToPlayer() {
        return mToPlayer;
    }

    public enum RequestType {Spectate, Play}
}
