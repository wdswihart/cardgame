package server.handlers.gameplay;

import com.corundumstudio.socketio.SocketIOClient;
import com.google.inject.Inject;
import de.saxsys.mvvmfx.InjectViewModel;
import models.responses.GameState;
import server.GameServer;
import server.core.gameplay.GameStateMachine;
import server.core.users.MatchmakingProvider;
import server.core.users.UsersProvider;
import server.handlers.BaseEventHandler;
import util.GuiceUtils;

public class QuitGameEventHandler extends BaseEventHandler<String> {
    @Inject
    private MatchmakingProvider mMatchmakingProvider;

    @Inject
    private UsersProvider mUserProvider;

    public static BaseEventHandler<?> getHandler() {
        return GuiceUtils.getInjector().getInstance(QuitGameEventHandler.class);
    }

    @Override
    protected Class<String> getDataClass() {
        return String.class;
    }

    @Override
    protected void handle(SocketIOClient client, String model) {
        //For now, remove the guy from the game.
        GameServer.User user = mUserProvider.getUsers().get(client.getSessionId().toString());
        if (user == null) {
            return;
        }

        mMatchmakingProvider.quitGame(client);
    }
}
