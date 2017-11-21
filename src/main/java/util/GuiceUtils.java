package util;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import di.DependencyModules;

import java.util.ArrayList;
import java.util.List;

public class GuiceUtils {
    private static Injector sInjector;

    public static Injector getInjector() {
        if (sInjector == null) {
            sInjector = Guice.createInjector(new DependencyModules());
        }
        return sInjector;
    }
}
