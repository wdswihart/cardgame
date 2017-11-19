package di;

import client.core.socketio.SocketIOClientProviderImpl;
import com.google.inject.AbstractModule;
import client.core.socketio.SocketIOClientProvider;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SocketIOClientProvider.class).toInstance(SocketIOClientProviderImpl.getInstance());
    }
}
