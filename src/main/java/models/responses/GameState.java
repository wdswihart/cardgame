package models.responses;

import models.Card;
import models.ModelBase;
import models.Player;

import java.util.ArrayList;
import java.util.List;

public class GameState extends ModelBase {
    private Player mPlayerOne = new Player();
    private Player mPlayerTwo = new Player();

    private Player mActivePlayer = new Player();

    private List<Card> mPlayerOneHand = new ArrayList<>();
    private List<Card> mPlayerTwoHand = new ArrayList<>();

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
    //endregion
}
