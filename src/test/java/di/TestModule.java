package di;

import client.core.ConnectionProvider;
import client.core.navigation.INavigationProvider;
import client.core.socketio.SocketIOClientProviderImpl;
import com.google.inject.AbstractModule;
import client.core.socketio.SocketIOClientProvider;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ConnectionProvider.class).toInstance(TestConnectionProviderImpl.getInstance());
        bind(SocketIOClientProvider.class).toInstance(SocketIOClientProviderImpl.getInstance());
        bind(INavigationProvider.class).toInstance(TestNavigationProvider.getInstance());
    }
}
