package models.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameStateList implements Serializable{

    private List<GameState> mGameStates = new ArrayList<>();

    public GameStateList() {

    }

    public GameStateList(List<GameState> gameStates) {
        mGameStates = gameStates;
    }

    public void setGameStates(List<GameState> gamesStates) {
        mGameStates = gamesStates;
    }

    public List<GameState> getGameStates() {
        return mGameStates;
    }
}
