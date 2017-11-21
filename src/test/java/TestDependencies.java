import com.google.inject.Guice;
import com.google.inject.Injector;
import di.TestNavigationModule;

class TestDependencies {
    //There is a better way to do this, but for the sake of showing a use of DI
    //setup all of our testing dependencies. Currently just the NavigationProvider
    //because it contains JavaFX UI elements.
    private static Injector sInjector;

    public static Injector getInjector() {
        if (sInjector == null) {
            sInjector = Guice.createInjector(new TestNavigationModule());
        }
        return sInjector;
    }
}
