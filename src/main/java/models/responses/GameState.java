package models.responses;

import models.ModelBase;
import models.Player;

public class GameState extends ModelBase {
    private Player mPlayerOne = new Player();
    private Player mPlayerTwo = new Player();

    public GameState(Player playerOne, Player playerTwo) {
        mPlayerOne = playerOne;
        mPlayerTwo = playerTwo;
    }

    public GameState() {

    }

    @Override
    public boolean isDefault() {
        //TODO: Should this be &&?
        return mPlayerOne.isDefault() || mPlayerTwo.isDefault();
    }

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
}
