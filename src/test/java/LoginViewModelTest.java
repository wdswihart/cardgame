import client.ui.DraggableView.DraggableView;
import client.ui.home.HomeView;
import client.ui.login.LoginViewModel;
import models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(JUnitPlatform.class)
public class LoginViewModelTest extends BaseTest {
    LoginViewModel loginViewModel;

    private final String errorMessage = "Invalid username or password.";

    @BeforeEach
    public void setup() {
        loginViewModel = TestDependencies.getInjector().getInstance(LoginViewModel.class);
    }

    @Test
    public void errorMessage_entersIncorrectUsernamePassword_firstTime() {
        connectionProvider.getAuthenticatedUser().setValue(new Player());
        verify(navigationProvider, never()).navigateTo(any());
        assertEquals(errorMessage, loginViewModel.getErrorProperty().getValue());
    }

    @Test
    public void successfulLogin_navigatesToHomeView() {
        connectionProvider.getAuthenticatedUser().setValue(new Player("test", "test"));
        verify(navigationProvider).navigateTo(DraggableView.class);
        assertEquals(errorMessage, "");
    }


}
