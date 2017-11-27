package di;

import client.core.navigation.INavigationProvider;
import client.core.navigation.NavigationProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.mockito.Mockito.mock;

public class TestNavigationProvider {
    private List<Class> mNavigationHistory = new ArrayList<>();
    private Stack<Class> mNavigationStack = new Stack<>();

    private static INavigationProvider sTestNavigationProvider;

    public static INavigationProvider getInstance() {
        if (sTestNavigationProvider == null) {
            sTestNavigationProvider = mock(INavigationProvider.class);
        }
        return sTestNavigationProvider;
    }
}
