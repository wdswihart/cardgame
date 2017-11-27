package di;

import com.google.inject.Binder;
import com.google.inject.Module;
import client.core.navigation.INavigationProvider;

public class TestNavigationModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(INavigationProvider.class).toInstance(TestNavigationProvider.getInstance());
    }
}
