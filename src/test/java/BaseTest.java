import client.core.ConnectionProvider;
import client.core.navigation.INavigationProvider;
import client.core.navigation.NavigationProvider;
import di.TestConnectionProviderImpl;
import di.TestNavigationProvider;

public class BaseTest {
    protected INavigationProvider navigationProvider = TestNavigationProvider.getInstance();
    protected ConnectionProvider connectionProvider = TestConnectionProviderImpl.getInstance();
}
