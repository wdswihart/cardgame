package di;

import client.core.ConnectionProvider;
import client.core.ConnectionProviderImpl;
import client.core.navigation.INavigationProvider;
import client.core.navigation.NavigationProvider;
import client.core.socketio.SocketIOProvider;
import client.core.socketio.SocketIOProviderImpl;
import com.google.inject.AbstractModule;
import server.configuration.ConfigurationProvider;
import server.configuration.ConfigurationProviderImpl;
import storage.StorageProvider;
import storage.StorageProviderImpl;

public class DependencyModules extends AbstractModule {
    @Override
    protected void configure() {
        bind(ConnectionProvider.class).to(ConnectionProviderImpl.class);

        bind(INavigationProvider.class).toInstance(NavigationProvider.getInstance());
        bind(SocketIOProvider.class).toInstance(SocketIOProviderImpl.getInstance());
        bind(StorageProvider.class).toInstance(StorageProviderImpl.getInstance());
        bind(ConfigurationProvider.class).toInstance(ConfigurationProviderImpl.getInstance());
    }
}
