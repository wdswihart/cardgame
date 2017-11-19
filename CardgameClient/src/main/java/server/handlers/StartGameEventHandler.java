package server.handlers;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import models.requests.GameRequest;
import server.core.users.MatchmakingProvider;
import util.GuiceUtils;

public class StartGameEventHandler extends BaseEventHandler<GameRequest>{

    @Inject
    private MatchmakingProvider mMatchmakingProvider;

    public static StartGameEventHandler getHandler() {
        return GuiceUtils.getInjector().getInstance(StartGameEventHandler.class);
    }

    @Override
    protected Class<GameRequest> getDataClass() {
        return GameRequest.class;
    }

    @Override
    protected void handle(SocketIOClient client, GameRequest model) {
        if (model.getRequestType() == GameRequest.RequestType.Play) {
            if (!mMatchmakingProvider.isGameCreated(model)) {
                mMatchmakingProvider.sendPlayRequest(client, model);
            }
            else {
                mMatchmakingProvider.acceptPlayRequest(client, model);
            }
        }
        else if (model.getRequestType() == GameRequest.RequestType.Spectate) {
            if (mMatchmakingProvider.isGameCreated(model)) {
                mMatchmakingProvider.spectate(client, model);
            }
        }
    }
}
