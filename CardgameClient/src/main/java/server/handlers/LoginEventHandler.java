package server.handlers;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import models.Events;
import models.Player;
import server.GameServer;
import server.core.users.ActiveUserProvider;
import storage.StorageProvider;
import util.GuiceUtils;
import util.JSONUtils;

import java.util.Map;

public class LoginEventHandler extends BaseEventHandler<Player> {
    public static LoginEventHandler getHandler() {
        return GuiceUtils.getInjector().getInstance(LoginEventHandler.class);
    }

    public LoginEventHandler() {

    }

    @Override
    protected Player deserialize(String data) {
        Player player = JSONUtils.fromJson(data, Player.class);
        player = (player == null) ? new Player() : player;
        return player;
    }

    @Override
    protected void handle(SocketIOClient client, Player model) {
        Map<String, Player> registeredUsers = mStorageProvider.getRegisteredUsers();

        if (model.isDefault() ||
                !registeredUsers.containsKey(model.getUsername()) ||
                !registeredUsers.get(model.getUsername()).getPassword().equals(model.getPassword())) {
            client.sendEvent(Events.LOGIN, new Player());

            mActiveUserProvider.removeUser(new GameServer.User(client, new Player()));
            return;
        }


        client.joinRoom("lobby");
        client.sendEvent(Events.LOGIN, model);
        mActiveUserProvider.addUser(new GameServer.User(client, model));
    }
}
