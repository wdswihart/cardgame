package client.ui.login;

import client.core.ConnectionProvider;
import client.core.navigation.INavigationProvider;
import client.model.User;
import client.ui.BaseViewModel;
import client.ui.HomeView.HomeView;
import com.google.inject.Inject;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.SimpleStringProperty;

import java.util.Observable;
import java.util.Observer;

public class LoginViewModel extends BaseViewModel {
    // METHODS:

    private SimpleStringProperty mUsernameProperty = new SimpleStringProperty("");
    private SimpleStringProperty mPasswordProperty = new SimpleStringProperty("");

    private Command mLoginCommand;

    // CONSTRUCTORS:

    @Inject
    public LoginViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider) {
        super(connectionProvider, navigationProvider);
        mConnectionProvider.getCurrentUser().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isDefault()) {
                mNavigationProvider.navigateTo(HomeView.class);
                mErrorProperty.setValue("");
            }
            else if ((oldVal == null || oldVal.isDefault()) && newVal.isDefault()) {
                //If we are returned a default user, and our old user is default or null, show error.
                mErrorProperty.setValue("Invalid username or password.");
            }
            else {
                //TODO: Logout, it doesn't go here actually, but somewhere else. Maybe the BaseViewModel.
                mErrorProperty.setValue("");
            }
        });
        mLoginCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                mConnectionProvider.loginUser(mUsernameProperty.getValue(), mPasswordProperty.getValue());
            }
        });
    }

    public SimpleStringProperty getUsernameProperty() {
        return mUsernameProperty;
    }

    public SimpleStringProperty getPasswordProperty() {
        return mPasswordProperty;
    }

    public Command getLoginCommand() {
        return mLoginCommand;
    }
}
