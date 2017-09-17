package core.di;

import com.google.inject.AbstractModule;
import core.navigation.INavigationProvider;
import core.navigation.NavigationProvider;

public class NavigationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(INavigationProvider.class).toInstance(NavigationProvider.getInstance());
    }
}
