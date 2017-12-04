package server.handlers;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.inject.Inject;
import de.saxsys.mvvmfx.InjectViewModel;
import server.core.users.MatchmakingProvider;
import server.core.users.UsersProvider;
import storage.StorageProvider;
import util.JSONUtils;

public abstract class BaseEventHandler <T> implements DataListener<String>{
    @Inject
    protected StorageProvider mStorageProvider;
    @Inject
    protected UsersProvider mUsersProvider;

    @Inject
    protected MatchmakingProvider mMatchmakingProvider;

    @Override
    public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
        try {
            if (getDataClass().equals(String.class)) {
                T string = (T)data;
                handle(client, string);
                return;
            }

            System.out.println("[SERVER]: Received event :" + data);
            T model = deserialize(data);
            handle(client, model);
        }
        catch (Exception e) {
            System.out.println("Error parsing JSON.");
            e.printStackTrace();
        }
    }

    protected T deserialize(String data) throws IllegalAccessException, InstantiationException {
        T model = JSONUtils.fromJson(data, getDataClass());

        if (model == null) {
            model = getDataClass().newInstance();
        }

        return model;
    }
    protected abstract Class<T> getDataClass();
    protected abstract void handle(SocketIOClient client, T model);
}
