package client.core.di;

import com.google.inject.AbstractModule;
import client.core.socketio.SocketIOProvider;
import client.core.socketio.SocketIOProviderImpl;

public class SocketIOModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SocketIOProvider.class).toInstance(SocketIOProviderImpl.getInstance());
    }
}
