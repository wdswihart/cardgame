import client.ui.createaccount.CreateAccountViewModel;
import models.Player;
import org.junit.Test;
import org.junit.platform.runner.JUnitPlatform;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.runner.RunWith;
import server.GameServer;

@RunWith(JUnitPlatform.class)
public class CreateAccountViewModelTest extends BaseTest {

    private final String errorMessage = "Passwords did not match.";



    @Test // this one works
    public void errorMessage_entersNonMatchingPasswords() {
        CreateAccountViewModel createAccountViewModel = TestDependencies.getInjector().getInstance(CreateAccountViewModel.class);
        createAccountViewModel.getPasswordProperty().setValue("x");
        createAccountViewModel.getPasswordAgainProperty().setValue("y");
        createAccountViewModel.getCreateAccountCommand().execute();
        assertEquals(errorMessage, createAccountViewModel.getErrorProperty().getValue());
        verify(navigationProvider, never()).navigatePrevious();

    }

    @Test
    public void createAccountCalled_NonMatchingPasswords_accountNotCreated() {
        CreateAccountViewModel createAccountViewModel = TestDependencies.getInjector().getInstance(CreateAccountViewModel.class);
        createAccountViewModel.getPasswordProperty().setValue("x");
        createAccountViewModel.getPasswordAgainProperty().setValue("y");
        createAccountViewModel.getCreateAccountCommand().execute();
        assertEquals(errorMessage, createAccountViewModel.getErrorProperty().getValue());
        connectionProvider.createAccount(createAccountViewModel.getPasswordProperty().getValue(), createAccountViewModel.getPasswordAgainProperty().getValue());
        verify(navigationProvider, never()).navigatePrevious();

    }

    @Test
    public void validCreateAccount_AccountCreated_navigatePrevious() {
        CreateAccountViewModel createAccountViewModel = TestDependencies.getInjector().getInstance(CreateAccountViewModel.class);
        createAccountViewModel.getPasswordProperty().setValue("x");
        createAccountViewModel.getPasswordAgainProperty().setValue("x");
        createAccountViewModel.getCreateAccountCommand().execute();
        connectionProvider.createAccount(createAccountViewModel.getUsernameProperty().getValue(), createAccountViewModel.getPasswordProperty().getValue());
        verify(navigationProvider).navigatePrevious();
    }
}