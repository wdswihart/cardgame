import client.core.ConnectionProvider;
import client.core.navigation.INavigationProvider;
import client.core.navigation.NavigationProvider;
import di.TestConnectionProviderImpl;
import di.TestNavigationProvider;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected INavigationProvider navigationProvider = TestNavigationProvider.getInstance();
    protected ConnectionProvider connectionProvider = TestConnectionProviderImpl.getInstance();

    @BeforeEach
    protected void setup() {
        TestNavigationProvider.resetInstance();
        TestConnectionProviderImpl.resetInstance();

        navigationProvider = TestNavigationProvider.getInstance();
        connectionProvider = TestConnectionProviderImpl.getInstance();
    }
}
