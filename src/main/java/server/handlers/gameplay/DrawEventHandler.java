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

public class DrawEventHandler extends BaseGameplayEventHandler<DrawRequest> {

    @Override
    protected Class<DrawRequest> getDataClass() {
        return DrawRequest.class;
    }

    @Override
    protected void handle(DrawRequest model) {
        getGameStateMachine().fire(GameStateMachine.Trigger.Draw);
    }

    @Override
    protected GameState.State getValidState() {
        return GameState.State.Draw;
    }

    public static BaseEventHandler<?> getHandler() {
        return GuiceUtils.getInjector().getInstance(DrawEventHandler.class);
    }
}
