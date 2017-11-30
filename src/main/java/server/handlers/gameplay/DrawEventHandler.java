package server.handlers.gameplay;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import models.Player;
import models.requests.DrawRequest;
import models.responses.GameState;
import server.core.gameplay.GameStateMachine;
import server.core.users.MatchmakingProvider;
import server.handlers.BaseEventHandler;
import util.GuiceUtils;

public class DrawEventHandler extends BaseEventHandler<DrawRequest> {
    @Inject
    private MatchmakingProvider mMatchmakingProvider;

    @Override
    protected Class<DrawRequest> getDataClass() {
        return DrawRequest.class;
    }

    @Override
    protected void handle(SocketIOClient client, DrawRequest model) {
        GameStateMachine gsm = mMatchmakingProvider.getGameStateMachine(client.getSessionId().toString());

        if (gsm == null || gsm.getState() != GameState.State.Draw) {
            return;
        }

        Player activePlayer = gsm.getGameState().getActivePlayer();
        Player playerForSocket = mUsersProvider.getUsers().get(client.getSessionId().toString()).getPlayer();

        if (playerForSocket == null || !activePlayer.getUsername().equals(playerForSocket.getUsername())) {
            //We don't care about this guy, its not his turn.
            return;
        }

        gsm.fire(GameStateMachine.Trigger.Draw);
    }

    public static BaseEventHandler<?> getHandler() {
        return GuiceUtils.getInjector().getInstance(DrawEventHandler.class);
    }
}
