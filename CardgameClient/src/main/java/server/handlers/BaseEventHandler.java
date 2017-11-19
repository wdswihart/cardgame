package server.handlers;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.google.inject.Inject;
import server.core.users.ActiveUserProvider;
import storage.StorageProvider;

public abstract class BaseEventHandler <T> implements DataListener<String>{
    @Inject
    protected StorageProvider mStorageProvider;
    @Inject
    protected ActiveUserProvider mActiveUserProvider;

    @Override
    public void onData(SocketIOClient client, String data, AckRequest ackSender) throws Exception {
        T model = deserialize(data);
        handle(client, model);
    }

    protected abstract T deserialize(String data);
    protected abstract void handle(SocketIOClient client, T model);
}
