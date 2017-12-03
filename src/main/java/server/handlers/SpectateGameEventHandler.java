package server.handlers;

import com.corundumstudio.socketio.SocketIOClient;
import models.Events;
import models.requests.GameRequest;
import models.responses.GameState;
import util.GuiceUtils;

public class SpectateGameEventHandler extends BaseEventHandler<GameState> {
    public static BaseEventHandler<?> getHandler() {
        return GuiceUtils.getInjector().getInstance(SpectateGameEventHandler.class);
    }

    @Override
    protected Class<GameState> getDataClass() {
        return GameState.class;
    }

    @Override
    protected void handle(SocketIOClient client, GameState model) {
        GameRequest request = new GameRequest();
        request.setFromPlayer(model.getPlayerOne());
        request.setToPlayer(model.getPlayerTwo());

        if (!mMatchmakingProvider.isGameCreated(request)) {
            client.sendEvent(Events.START_GAME, new GameState());
            return;
        }

        mMatchmakingProvider.spectate(client, request);
    }
}
