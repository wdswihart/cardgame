package server.handlers.gameplay;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import models.Events;
import server.GameServer;
import server.core.gameplay.GameStateMachine;
import server.core.socketio.SocketIOServerProvider;
import server.handlers.BaseEventHandler;
import util.GuiceUtils;

public class GameChatEventHandler extends BaseEventHandler<String> {
    public static BaseEventHandler<?> getHandler() {
        return GuiceUtils.getInjector().getInstance(GameChatEventHandler.class);
    }

    @Inject
    private SocketIOServerProvider mServerProvider;

    @Override
    protected Class<String> getDataClass() {
        return String.class;
    }

    @Override
    protected void handle(SocketIOClient client, String model) {
        GameStateMachine gsm = mMatchmakingProvider.getGameStateMachine(client.getSessionId().toString());

        if (gsm == null) {
            return;
        }

        GameServer.User user = mUsersProvider.getUsers().get(client.getSessionId().toString());
        mServerProvider.broadcastRoom(gsm.getGuid(), Events.GAME_CHAT, user.getPlayer().getUsername() + ": " + model);
    }
}
