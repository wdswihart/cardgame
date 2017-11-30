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

    //Since it is string data, we don't want it deserialized.
    @Override
    protected String deserialize(String data) {
        return data;
    }

    @Override
    protected Class<String> getDataClass() {
        return String.class;
    }

    @Override
    protected void handle(SocketIOClient client, String model) {
        if (!mUsersProvider.getUsers().containsKey(client.getSessionId().toString())) {
            return;
        }

        GameServer.User user = mUsersProvider.getUsers().get(client.getSessionId().toString());
        mServerProvider.broadcast(Events.CHAT, user.getPlayer().getUsername() + ": " + model);
    }
}
