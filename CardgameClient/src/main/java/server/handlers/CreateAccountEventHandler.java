package server.handlers;

import client.ui.createaccount.CreateAccountView;
import com.corundumstudio.socketio.SocketIOClient;
import models.Events;
import models.Player;
import util.GuiceUtils;
import util.JSONUtils;

import java.util.Map;

public class CreateAccountEventHandler extends BaseEventHandler<Player> {
    public static CreateAccountEventHandler getHandler() {
        return GuiceUtils.getInjector().getInstance(CreateAccountEventHandler.class);
    }

    public CreateAccountEventHandler() {

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

        if (registeredUsers.containsKey(model.getUsername())) {
            //Error existing mPlayer.
            client.sendEvent(Events.CREATE_ACCOUNT, new Player());
            return;
        }

        mStorageProvider.addRegisteredUser(model);
        client.sendEvent(Events.CREATE_ACCOUNT, model);
    }
}
