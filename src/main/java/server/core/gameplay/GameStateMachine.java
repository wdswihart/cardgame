package server.core.gameplay;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.google.inject.Inject;
import models.Card;
import models.Events;
import models.responses.GameState;
import server.GameServer;
import server.core.users.UsersProvider;

public class GameStateMachine {
    private GameState mGameState = new GameState();

    private GameServer.User mUserOne;
    private GameServer.User mUserTwo;

    //region StateMachine States/Triggers

    private StateMachine<State, Trigger> mStateMachine;
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
    public enum Trigger {
        PlayersReady,
        Attack,
        Draw,
        MainPass,

//        Refresh
//        MainAttack,
//        Damage,
//        Defend,
//        Maintain,
    }
    //endregion

    @Inject
    public GameStateMachine(GameState gameState, UsersProvider usersProvider) {
        mGameState = gameState;

        mUserOne = usersProvider.getUserByUsername(mGameState.getPlayerOne().getUsername());
        mUserTwo = usersProvider.getUserByUsername(mGameState.getPlayerTwo().getUsername());

        StateMachineConfig<State, Trigger> config = new StateMachineConfig<>();

        //Initial state, only come here once.
        config.configure(State.Waiting)
                .permit(Trigger.PlayersReady, State.Draw)
                .onExit(this::exitWaiting);

        config.configure(State.Draw)
                .permit(Trigger.Draw, State.Main)
                .onExit(this::exitDraw);

        config.configure(State.Main)
                .permit(Trigger.MainPass, State.Draw)
                .onExit(this::exitMain);


//TODO: Leaving the old to prevent losing it.
//        config.configure(State.Refresh)
//                .permit(Trigger.Refresh, State.Draw);
//        config.configure(State.Maintain)
//                .permit(Trigger.Maintain, State.Draw);
//        config.configure(State.Draw)
//                .permit(Trigger.Draw, State.Main);
//        config.configure(State.Main)
//                .permit(Trigger.MainAttack, State.Attackers)
//                .permit(Trigger.MainPass, State.End);
//        config.configure(State.Attackers)
//                .permit(Trigger.Attack, State.Defenders);
//        config.configure(State.Defenders)
//                .permit(Trigger.Defend, State.Damage);
//        config.configure(State.Damage)
//                .permit(Trigger.Damage, State.Main);



        mStateMachine = new StateMachine<State, Trigger>(State.Waiting, config);
    }

    private void exitWaiting() {
        //Need to deal some cards here.
        mGameState.getPlayerOneHand().add(new Card());
        mGameState.getPlayerOneHand().add(new Card());
        mGameState.getPlayerOneHand().add(new Card());
        mGameState.getPlayerOneHand().add(new Card());
        mGameState.getPlayerOneHand().add(new Card());

        mGameState.getPlayerTwoHand().add(new Card());
        mGameState.getPlayerTwoHand().add(new Card());
        mGameState.getPlayerTwoHand().add(new Card());
        mGameState.getPlayerTwoHand().add(new Card());

        //Set the default active player to player 1.
        mGameState.setActivePlayer(mGameState.getPlayerOne());

        broadcastToPlayers(Events.UPDATE_GAME, mGameState);
    }

    private void exitDraw() {

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
}
