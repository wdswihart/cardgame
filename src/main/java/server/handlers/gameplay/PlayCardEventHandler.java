package server.handlers.gameplay;

import com.corundumstudio.socketio.SocketIOClient;
import models.requests.PlayCardRequest;
import models.responses.GameState;
import server.core.gameplay.GameStateMachine;
import server.handlers.BaseEventHandler;
import util.GuiceUtils;

public class PlayCardEventHandler extends BaseGameplayEventHandler<PlayCardRequest> {
    public static PlayCardEventHandler getHandler() {
        return GuiceUtils.getInjector().getInstance(PlayCardEventHandler.class);
    }


    @Override
    protected Class<PlayCardRequest> getDataClass() {
        return PlayCardRequest.class;
    }

    @Override
    protected void handle(PlayCardRequest model) {
        GameStateMachine gsm = getGameStateMachine();
        gsm.fireWith(GameStateMachine.Trigger.PlayCard, model.getCard());
    }

    @Override
    protected GameState.State getValidState() {
        return GameState.State.Main;
    }
}
