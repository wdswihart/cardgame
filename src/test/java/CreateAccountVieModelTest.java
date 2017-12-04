import client.ui.createaccount.CreateAccountViewModel;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@Category(UnitTest.class)
@RunWith(JUnitPlatform.class)
public class CreateAccountVieModelTest extends BaseTest {
    private final String errorMessage = "Passwords did not match.";

    @BeforeEach
    protected void setup() {
        super.setup();
    }

    @Test
    public void errorMessage_entersNonMatchingPasswords() {
        CreateAccountViewModel createAccountViewModel = new CreateAccountViewModel(connectionProvider, navigationProvider);

        createAccountViewModel.getPasswordProperty().setValue("x");
        createAccountViewModel.getPasswordAgainProperty().setValue("y");
        createAccountViewModel.getCreateAccountCommand().execute();
        assertEquals(errorMessage, createAccountViewModel.getErrorProperty().getValue());
        verify(navigationProvider, never()).navigatePrevious();

    }

    @Test
    public void createAccountCalled_NonMatchingPasswords_accountNotCreated() {
        CreateAccountViewModel createAccountViewModel = new CreateAccountViewModel(connectionProvider, navigationProvider);

        createAccountViewModel.getPasswordProperty().setValue("x");
        createAccountViewModel.getPasswordAgainProperty().setValue("y");
        createAccountViewModel.getCreateAccountCommand().execute();
        assertEquals(errorMessage, createAccountViewModel.getErrorProperty().getValue());
        connectionProvider.createAccount(createAccountViewModel.getPasswordProperty().getValue(), createAccountViewModel.getPasswordAgainProperty().getValue());
        verify(navigationProvider, never()).navigatePrevious();

    }

    @Test
    public void validCreateAccount_AccountCreated_navigatePrevious() {
        CreateAccountViewModel createAccountViewModel = new CreateAccountViewModel(connectionProvider, navigationProvider);

        createAccountViewModel.getPasswordProperty().setValue("x");
        createAccountViewModel.getPasswordAgainProperty().setValue("x");
        createAccountViewModel.getCreateAccountCommand().execute();
        verify(navigationProvider).navigatePrevious();
    }
}