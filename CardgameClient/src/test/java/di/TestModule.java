package di;

import client.core.socketio.SocketIOProviderImpl;
import com.google.inject.AbstractModule;
import client.core.socketio.SocketIOProvider;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SocketIOProvider.class).toInstance(SocketIOProviderImpl.getInstance());
    }
}
