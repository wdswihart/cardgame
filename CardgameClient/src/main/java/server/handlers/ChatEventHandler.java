package server.handlers;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import models.Events;
import server.GameServer;
import server.core.socketio.SocketIOServerProvider;
import util.GuiceUtils;

public class ChatEventHandler extends BaseEventHandler<String> {
    @Inject
    private SocketIOServerProvider mServerProvider;

    public static ChatEventHandler getHandler() {
        return GuiceUtils.getInjector().getInstance(ChatEventHandler.class);
    }

    public ChatEventHandler() {

    }

    @Override
    protected String deserialize(String data) {
        return data;
    }

    @Override
    protected void handle(SocketIOClient client, String model) {
        if (!mActiveUserProvider.getActiveUsers().containsKey(client.getSessionId().toString())) {
            return;
        }

        GameServer.User user = mActiveUserProvider.getActiveUsers().get(client.getSessionId().toString());
        mServerProvider.broadcast(Events.CHAT, user.getPlayer().getUsername() + ": " + model);
    }
}
