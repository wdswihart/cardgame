package server.handlers;

import com.corundumstudio.socketio.SocketIOClient;
import models.Events;
import models.Player;
import server.GameServer;
import util.GuiceUtils;

import java.util.Map;

public class LoginEventHandler extends BaseEventHandler<Player> {
    public static LoginEventHandler getHandler() {
        return GuiceUtils.getInjector().getInstance(LoginEventHandler.class);
    }

    @Override
    protected Class<Player> getDataClass() {
        return Player.class;
    }

    @Override
    protected void handle(SocketIOClient client, Player model) {
        Map<String, Player> registeredUsers = mStorageProvider.getRegisteredUsers();

        if (model.isDefault() ||
                !registeredUsers.containsKey(model.getUsername()) ||
                !registeredUsers.get(model.getUsername()).getPassword().equals(model.getPassword())) {
            client.sendEvent(Events.LOGIN, new Player());

            mUsersProvider.removeUser(new GameServer.User(client, new Player()));
            return;
        }


        client.joinRoom("lobby");
        client.sendEvent(Events.LOGIN, model);
        mUsersProvider.addUser(new GameServer.User(client, model));
    }
}
