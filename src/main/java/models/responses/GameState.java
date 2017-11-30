package models.responses;

import models.Card;
import models.ModelBase;
import models.Player;

import java.util.ArrayList;
import java.util.List;

public class GameState extends ModelBase {
    public enum State {
        Waiting,
        Draw,
        Main,
//        End,
//        Refresh,
//        Maintain,
//        Attackers,
//        Defenders,
//        Damage,
    }

    private State mState = State.Waiting;

    private Player mPlayerOne = new Player();
    private Player mPlayerTwo = new Player();

    private Player mActivePlayer = new Player();

    private List<Card> mPlayerOneHand = new ArrayList<>();
    private List<Card> mPlayerTwoHand = new ArrayList<>();

    private List<Card> mPlayerOneDeck = new ArrayList<>();
    private List<Card> mPlayerTwoDeck = new ArrayList<>();

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

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        this.mState = state;
    }
    //endregion
}
