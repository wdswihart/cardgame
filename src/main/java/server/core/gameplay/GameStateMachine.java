package server.core.gameplay;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.google.inject.Inject;
import javafx.collections.ObservableList;
import models.Card;
import models.Events;
import models.Player;
import models.responses.GameState;
import server.GameServer;
import server.configuration.ConfigurationProvider;
import server.core.users.UsersProvider;
import models.responses.GameState.State;

import java.util.List;
import java.util.Random;

public class GameStateMachine {
    private ConfigurationProvider mConfigurationProvider;

    private GameState mGameState = new GameState();

    private GameServer.User mUserOne;
    private GameServer.User mUserTwo;

    //region StateMachine States/Triggers

    private StateMachine<GameState.State, Trigger> mStateMachine;
    public enum Trigger {
        PlayersReady,
        Attack,
        Draw,
        MainPass,
    }
    //endregion

    @Inject
    public GameStateMachine(GameState gameState, UsersProvider usersProvider, ConfigurationProvider configurationProvider) {
        mConfigurationProvider = configurationProvider;
        mGameState = gameState;

        mUserOne = usersProvider.getUserByUsername(mGameState.getPlayerOne().getUsername());
        mUserTwo = usersProvider.getUserByUsername(mGameState.getPlayerTwo().getUsername());

        StateMachineConfig<GameState.State, Trigger> config = new StateMachineConfig<>();

        //Initial state, only come here once.
        config.configure(State.Waiting)
                .permit(Trigger.PlayersReady, State.Draw)
                .onExit(this::exitWaiting);

        config.configure(State.Draw)
                .permit(Trigger.Draw, State.Main)
                .onEntry(this::enterDraw)
                .onExit(this::exitDraw);

        config.configure(State.Main)
                .permit(Trigger.MainPass, State.Draw)
                .onEntry(this::enterMain)
                .onExit(this::exitMain);

      mStateMachine = new StateMachine<State, Trigger>(State.Waiting, config);

      setupSubscriptions();
    }

    private void setupSubscriptions() {

    }

    private void exitWaiting() {
        //TODO: Maybe assign a random deck for now? (shrug)
        addCardsToDeck(mGameState.getPlayerOneDeck(), 50);
        addCardsToDeck(mGameState.getPlayerTwoDeck(), 45);

        dealCards();

        broadcastToPlayers(Events.UPDATE_GAME, mGameState);
    }

    private void addCardsToDeck(List<Card> playerOneDeck, int i) {
        for (int x = 0; x < i; x++) {
            playerOneDeck.add(new Card("monster " + x));
        }
    }

    private void enterDraw() {
        mGameState.setState(State.Draw);

        //Set the default active player to player 1.
        if (!mGameState.getActivePlayer().equals(mGameState.getPlayerOne())) {
            mGameState.setActivePlayer(mGameState.getPlayerOne());
        }
        else {
            mGameState.setActivePlayer(mGameState.getPlayerTwo());
        }

        broadcastToPlayers(Events.UPDATE_GAME, mGameState);
    }

    private void exitDraw() {
        List<Card> deck = null;
        List<Card> hand = null;
        if (mGameState.getActivePlayer().equals(mGameState.getPlayerOne())) {
            deck = mGameState.getPlayerOneDeck();
            hand = mGameState.getPlayerOneHand();
        }
        else {
            deck = mGameState.getPlayerTwoDeck();
            hand = mGameState.getPlayerTwoHand();
        }

        int playerIdx = new Random().nextInt(deck.size());
        hand.add(deck.get(playerIdx));
        deck.remove(playerIdx);

        broadcastToPlayers(Events.UPDATE_GAME, mGameState);
    }

    private void enterMain() {
        mGameState.setState(State.Main);

        //For now, just wait a few seconds and pass.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fire(Trigger.MainPass);
    }
    private void exitMain() {

    }


    public State getState() {
        return mStateMachine.getState();
    }

    public GameState getGameState() {
        return mGameState;
    }

    public void fire(Trigger t) {
        mStateMachine.fire(t);
    }

    private void broadcastToPlayers(String event, GameState mGameState) {
        mUserOne.getClient().sendEvent(event, mGameState);
        mUserTwo.getClient().sendEvent(event, mGameState);
    }

    private void dealCards() {
        List<Card> playerOneDeck = mGameState.getPlayerOneDeck();
        List<Card> playerTwoDeck = mGameState.getPlayerTwoDeck();

        //Shuffle at some point.
        int maxHandSize = mConfigurationProvider.getMaxHandSize();

        for (int i = 0; i < maxHandSize; i++) {
            int playerOneIdx = new Random().nextInt(playerOneDeck.size());
            mGameState.getPlayerOneHand().add(playerOneDeck.get(playerOneIdx));
            playerOneDeck.remove(playerOneIdx);

            int playerTwoIdx = new Random().nextInt(playerTwoDeck.size());
            mGameState.getPlayerTwoHand().add(playerTwoDeck.get(playerTwoIdx));
            playerTwoDeck.remove(playerTwoIdx);
        }
    }
}
