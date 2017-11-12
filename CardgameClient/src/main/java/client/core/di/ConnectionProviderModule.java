package client.core.di;

import client.core.ConnectionProvider;
import client.core.ConnectionProviderImpl;
import com.google.inject.AbstractModule;

public class ConnectionProviderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ConnectionProvider.class).to(ConnectionProviderImpl.class);
    }
}
