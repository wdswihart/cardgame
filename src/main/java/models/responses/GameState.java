package models.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.Card;
import models.ModelBase;
import models.Player;

import java.util.ArrayList;
import java.util.List;

public class GameState extends ModelBase {

    public enum State {
        Waiting("Waiting"),
        Draw("Draw"),
        Main("Main"),
        Attack("Attack"),
        PlayingCard("PlayingCard"),
        EndGame("EndGame");

        private String mState = "";
        State(String state) {
            mState = state;
        }

        public String toString() {
            return mState;
        }
    }

    private State mState = State.Waiting;

    private Player mPlayerOne = new Player();
    private Player mPlayerTwo = new Player();

    private int mPlayerOneHealth = 20;
    private int mPlayerTwoHealth = 20;

    private Player mActivePlayer = new Player();

    private List<Card> mPlayerOneHand = new ArrayList<>();
    private List<Card> mPlayerTwoHand = new ArrayList<>();

    private List<Card> mPlayerOneDeck = new ArrayList<>();
    private List<Card> mPlayerTwoDeck = new ArrayList<>();

    private List<Card> mPlayerOneField = new ArrayList<>();
    private List<Card> mPlayerTwoField = new ArrayList<>();

    public GameState(Player playerOne, Player playerTwo) {
        mPlayerOne = playerOne;
        mPlayerTwo = playerTwo;
    }

    public GameState() {

    }

    @Override
    public boolean isDefault() {
        return mPlayerOne.isDefault() && mPlayerTwo.isDefault();
    }

    //region Getters/Setters
    public Player getPlayerOne() {
        return mPlayerOne;
    }

    public void setPlayerOne(Player player) {
        mPlayerOne = player;
    }

    public Player getPlayerTwo() {
        return mPlayerTwo;
    }

    public void setPlayerTwo(Player player) {
        mPlayerTwo = player;
    }

    public List<Card> getPlayerOneHand() {
        return mPlayerOneHand;
    }

    public void setPlayerOneHand(List<Card> playerOneHand) {
        this.mPlayerOneHand = playerOneHand;
    }

    public List<Card> getPlayerTwoHand() {
        return mPlayerTwoHand;
    }

    public void setPlayerTwoHand(List<Card> playerTwoHand) {
        this.mPlayerTwoHand = playerTwoHand;
    }

    public Player getActivePlayer() {
        return mActivePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.mActivePlayer = activePlayer;
    }

    public List<Card> getPlayerTwoDeck() {
        return mPlayerTwoDeck;
    }

    public void setPlayerTwoDeck(List<Card> playerTwoDeck) {
        this.mPlayerTwoDeck = playerTwoDeck;
    }

    public List<Card> getPlayerOneDeck() {
        return mPlayerOneDeck;
    }

    public void setPlayerOneDeck(List<Card> playerOneDeck) {
        this.mPlayerOneDeck = playerOneDeck;
    }

    public String getState() {
        return mState.toString();
    }

    public void setState(String state) {
        mState = State.valueOf(state);
    }

    @JsonIgnore
    public void setStateEnum(State state) {
        mState = state;
    }

    @JsonIgnore
    public State getStateEnum() {
        return mState;
    }

    public List<Card> getPlayerOneField() {
        return mPlayerOneField;
    }

    public void setPlayerOneField(List<Card> playerOneField) {
        this.mPlayerOneField = playerOneField;
    }

    public List<Card> getPlayerTwoField() {
        return mPlayerTwoField;
    }

    public void setPlayerTwoField(List<Card> playerTwoField) {
        this.mPlayerTwoField = playerTwoField;
    }

    public int getPlayerOneHealth() {
        return mPlayerOneHealth;
    }

    public void setPlayerOneHealth(int playerOneHealth) {
        this.mPlayerOneHealth = playerOneHealth;
    }

    public int getPlayerTwoHealth() {
        return mPlayerTwoHealth;
    }

    public void setPlayerTwoHealth(int playerTwoHealth) {
        this.mPlayerTwoHealth = playerTwoHealth;
    }
    //endregion
}
