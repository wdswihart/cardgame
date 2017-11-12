package client.core.di;

import com.google.inject.AbstractModule;
import client.core.navigation.INavigationProvider;
import client.core.navigation.NavigationProvider;

public class NavigationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(INavigationProvider.class).toInstance(NavigationProvider.getInstance());
    }
}
