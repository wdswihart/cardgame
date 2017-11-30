package server.handlers.gameplay;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import models.Player;
import models.requests.DrawRequest;
import models.responses.GameState;
import server.core.gameplay.GameStateMachine;
import server.core.users.MatchmakingProvider;
import server.handlers.BaseEventHandler;

public abstract class BaseGameplayEventHandler <T> extends BaseEventHandler<T> {
    @Inject
    private MatchmakingProvider mMatchmakingProvider;

    private GameStateMachine mGsm;

    protected GameStateMachine getGameStateMachine() {
        return mGsm;
    }

    @Override
    protected final void handle(SocketIOClient client, T model) {
        mGsm = mMatchmakingProvider.getGameStateMachine(client.getSessionId().toString());

        if (mGsm == null || mGsm.getState() != getValidState()) {
            return;
        }

        Player activePlayer = mGsm.getGameState().getActivePlayer();
        Player playerForSocket = mUsersProvider.getUsers().get(client.getSessionId().toString()).getPlayer();

        if (playerForSocket == null || !activePlayer.getUsername().equals(playerForSocket.getUsername())) {
            //We don't care about this guy, its not his turn.
            return;
        }

        handle(model);
    }

    protected abstract void handle(T model);
    protected abstract GameState.State getValidState();
}
