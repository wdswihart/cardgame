package server.core.gameplay;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.google.inject.Inject;
import models.Card;
import models.Events;
import models.Player;
import models.requests.AttackRequest;
import models.responses.CardList;
import models.responses.GameState;
import org.apache.commons.io.IOUtils;
import server.GameServer;
import server.configuration.ConfigurationProvider;
import server.core.users.UsersProvider;
import models.responses.GameState.State;
import util.JSONUtils;

import java.io.BufferedInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameStateMachine {
    private ConfigurationProvider mConfigurationProvider;

    private GameState mGameState = new GameState();

    private GameServer.User mUserOne;
    private GameServer.User mUserTwo;

    private List<GameServer.User> mSpectatorList = new ArrayList<>();

    //region StateMachine States/Triggers

    private StateMachine<GameState.State, Trigger> mStateMachine;
    private Object mTriggeredValue = new Object();

    public enum Trigger {
        PlayersReady,
        Attack,
        Draw,
        PlayCard,
        PassMain,
        PlayedCard,
        Defend,
        GameOver,
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
                .permit(Trigger.PassMain, State.Draw)
                .permit(Trigger.PlayCard, State.PlayingCard)
                .permit(Trigger.Attack, State.Attack)
                .permit(Trigger.GameOver, State.EndGame)
                .onEntry(this::enterMain)
                .onExit(this::exitMain);

        config.configure(State.PlayingCard)
                .permit(Trigger.PlayedCard, State.Main)
                .permit(Trigger.GameOver, State.EndGame)
                .onEntry(this::enterPlayingCard);

        config.configure(State.Attack)
                .permit(Trigger.Defend, State.Main)
                .permit(Trigger.GameOver, State.EndGame)
                .onEntry(this::enterAttack);

        config.configure(State.EndGame)
                .onEntry(this::enterEndGame);

      mStateMachine = new StateMachine<State, Trigger>(State.Waiting, config);

      setupSubscriptions();
    }

    private void setupSubscriptions() {

    }

    private void exitWaiting() {
        //TODO: Maybe assign a random deck for now? (shrug)
        addCardsToDeck(mGameState.getPlayerOneDeck());
        addCardsToDeck(mGameState.getPlayerTwoDeck());

        dealCards();

        broadcastToPlayers(Events.UPDATE_GAME, mGameState);
    }

    private void addCardsToDeck(List<Card> playerOneDeck) {
        String jsonString = null;
        try {
            jsonString = new String(IOUtils.toString(getClass().getResourceAsStream("/cards.json"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        CardList list = JSONUtils.fromJson(jsonString, CardList.class);

        if (list == null) {
            System.out.println("Error parsing cardList.");
            return;
        }

        for (Card c : list.getCards()) {
            playerOneDeck.add(c);
        }
    }

    private void enterDraw() {
        mGameState.setStateEnum(State.Draw);

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
        List<Card> deck = getActivePlayerDeck();
        List<Card> hand = getActivePlayerHand();

        int playerIdx = new Random().nextInt(deck.size());
        hand.add(deck.get(playerIdx));
        deck.remove(playerIdx);

        broadcastToPlayers(Events.UPDATE_GAME, mGameState);
    }

    private void enterMain() {
        mGameState.setStateEnum(State.Main);

        //TODO: Send list of actions player is able to do.

        broadcastToPlayers(Events.UPDATE_GAME, mGameState);
    }

    private void exitMain() {

    }

    private void enterPlayingCard() {
        Card requestedCard = (Card) mTriggeredValue;
        List<Card> activePlayerHand = getActivePlayerHand();

        //For now, we can allow it to be played on the contingency that it is in the players hand.
        if (activePlayerHand.contains(requestedCard)) {
            activePlayerHand.remove(requestedCard);
            getActivePlayerField().add(requestedCard);
        }

        mStateMachine.fire(Trigger.PlayedCard);
    }

    //region Attack
    private void enterAttack() {
        AttackRequest request = (AttackRequest)mTriggeredValue;

        for (Card card : request.getAttackingMonsters()) {
            if (canAttack(card)) {
                //For now we're just applying damage to other player.
                dealDamage(card);
            }
        }
        broadcastToPlayers(Events.UPDATE_GAME, mGameState);

        //Allow to directly return to main and pass for now.
        mStateMachine.fire(Trigger.Defend);
        mStateMachine.fire(Trigger.PassMain);
    }

    private void dealDamage(Card card) {
        //TODO: Put the cards/decks/hands/fields/heaths into Player. This is annoying.
        if (mGameState.getActivePlayer().getUsername().equals(mGameState.getPlayerOne().getUsername())) {
            int newHealth = mGameState.getPlayerTwoHealth() - card.getPower();
            newHealth = newHealth < 0 ? 0 : newHealth;
            mGameState.setPlayerTwoHealth(newHealth);
        }
        else {
            int newHealth = mGameState.getPlayerOneHealth() - card.getPower();
            newHealth = newHealth < 0 ? 0 : newHealth;
            mGameState.setPlayerOneHealth(newHealth);
        }
        if (mGameState.getPlayerTwoHealth() == 0 || mGameState.getPlayerOneHealth() == 0) {
            mStateMachine.fire(Trigger.GameOver);
        }
    }

    private boolean canAttack(Card card) {
        return getActivePlayerField().contains(card);
    }
    //endregion

    private void enterEndGame() {
        mGameState.setStateEnum(State.EndGame);
        broadcastToPlayers(Events.UPDATE_GAME, mGameState);
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

    public void fireWith(Trigger t, Object o) {
        mTriggeredValue = o;
        fire(t);
    }

    private void broadcastToPlayers(String event, GameState mGameState) {
        mUserOne.getClient().sendEvent(event, mGameState);
        mUserTwo.getClient().sendEvent(event, mGameState);

        for (GameServer.User user : mSpectatorList) {
            user.getClient().sendEvent(event, mGameState);
        }
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

    //Takes care of removing a player from the game.
    //Notifying an empty GameState will signal that the user should no longer be in the GameView.
    public void removeUser(GameServer.User user) {
        if (user.getPlayer().getUsername().equals(mGameState.getPlayerOne().getUsername())) {
            mUserOne.getClient().sendEvent(Events.UPDATE_GAME, new GameState());
        }
        else if (user.getPlayer().getUsername().equals(mGameState.getPlayerTwo().getUsername())) {
            mUserTwo.getClient().sendEvent(Events.UPDATE_GAME, new GameState());
        }
        else {
            user.getClient().sendEvent(Events.UPDATE_GAME, new GameState());
            mSpectatorList.remove(user);
            mGameState.removeSpectator(user.getPlayer());
        }
    }

    public void addSpectator(GameServer.User user) {
        user.getClient().sendEvent(Events.START_GAME, mGameState);
        mSpectatorList.add(user);
        mGameState.addSpectator(user.getPlayer());
    }


    //region Convenience Methods
    private List<Card> getActivePlayerHand() {
        if (mGameState.getActivePlayer().getUsername().equals(mGameState.getPlayerOne().getUsername())) {
            return mGameState.getPlayerOneHand();
        }
        return mGameState.getPlayerTwoHand();
    }

    private List<Card> getActivePlayerDeck() {
        if (mGameState.getActivePlayer().getUsername().equals(mGameState.getPlayerOne().getUsername())) {
            return mGameState.getPlayerOneDeck();
        }
        return mGameState.getPlayerTwoDeck();
    }

    private List<Card> getActivePlayerField() {
        if (mGameState.getActivePlayer().getUsername().equals(mGameState.getPlayerOne().getUsername())) {
            return mGameState.getPlayerOneField();
        }
        return mGameState.getPlayerTwoField();
    }

    private List<Card> getInactivePlayerHand() {
        if (mGameState.getActivePlayer().getUsername().equals(mGameState.getPlayerOne().getUsername())) {
            return mGameState.getPlayerTwoHand();
        }
        return mGameState.getPlayerOneHand();
    }

    private List<Card> getInactivePlayerDeck() {
        if (mGameState.getActivePlayer().getUsername().equals(mGameState.getPlayerOne().getUsername())) {
            return mGameState.getPlayerTwoDeck();
        }
        return mGameState.getPlayerOneDeck();
    }

    private List<Card> getInactivePlayerField() {
        if (mGameState.getActivePlayer().getUsername().equals(mGameState.getPlayerOne().getUsername())) {
            return mGameState.getPlayerTwoField();
        }
        return mGameState.getPlayerOneField();
    }
    //endregion
}
