package di;

import client.core.ConnectionProvider;
import client.core.ConnectionProviderImpl;
import client.core.navigation.INavigationProvider;
import client.core.navigation.NavigationProvider;
import client.core.socketio.SocketIOClientProvider;
import client.core.socketio.SocketIOClientProviderImpl;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.google.inject.AbstractModule;
import server.configuration.ConfigurationProvider;
import server.configuration.ConfigurationProviderImpl;
import server.core.socketio.SocketIOServerProvider;
import server.core.socketio.SocketIOServerProviderImpl;
import server.core.users.ActiveUserProvider;
import server.core.users.ActiveUserProviderImpl;
import server.core.users.MatchmakingProvider;
import server.core.users.MatchmakingProviderImpl;
import storage.StorageProvider;
import storage.StorageProviderImpl;

public class DependencyModules extends AbstractModule {
    @Override
    protected void configure() {
        bind(ConnectionProvider.class).to(ConnectionProviderImpl.class);

        bind(INavigationProvider.class).toInstance(NavigationProvider.getInstance());
        bind(SocketIOClientProvider.class).toInstance(SocketIOClientProviderImpl.getInstance());
        bind(StorageProvider.class).toInstance(StorageProviderImpl.getInstance());
        bind(ConfigurationProvider.class).toInstance(ConfigurationProviderImpl.getInstance());

        bind(SocketIOServerProvider.class).to(SocketIOServerProviderImpl.class);
        bind(ActiveUserProvider.class).to(ActiveUserProviderImpl.class);
        bind(MatchmakingProvider.class).to(MatchmakingProviderImpl.class);
    }
}
