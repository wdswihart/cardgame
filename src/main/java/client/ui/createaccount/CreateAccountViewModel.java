package client.ui.createaccount;

import client.core.ConnectionProvider;
import client.core.navigation.INavigationProvider;
import models.Player;
import client.ui.BaseViewModel;
import com.google.inject.Inject;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;

public class CreateAccountViewModel extends BaseViewModel {
    private SimpleStringProperty mUsernameProperty = new SimpleStringProperty();
    private SimpleStringProperty mPasswordProperty = new SimpleStringProperty();
    private SimpleStringProperty mPasswordAgainProperty = new SimpleStringProperty();

    private Command mCreateAccountCommand;

    @Inject
    public CreateAccountViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider) {
        super(connectionProvider, navigationProvider);

        mConnectionProvider.getCreatedUser().addListener(this::createdUserChanged);

        mCreateAccountCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                if (!mPasswordProperty.get().equals(mPasswordAgainProperty.get())) {
                    mErrorProperty.setValue("Passwords did not match.");
                    return;
                }
                mConnectionProvider.createAccount(mUsernameProperty.get(), mPasswordProperty.get());
                mErrorProperty.setValue("");
            }
        });
    }

    public Command getCreateAccountCommand() {
        return mCreateAccountCommand;
    }

    public SimpleStringProperty getPasswordAgainProperty() {
        return mPasswordAgainProperty;
    }

    public SimpleStringProperty getPasswordProperty() {
        return mPasswordProperty;
    }

    public SimpleStringProperty getUsernameProperty() {
        return mUsernameProperty;
    }

    private void createdUserChanged(Observable observable, Player oldPlayer, Player newPlayer) {
        if (!newPlayer.isDefault()) {
            //Success, navigate back to login screen.
            mNavigationProvider.navigatePrevious();
            mConnectionProvider.getCreatedUser().removeListener(this::createdUserChanged);
        }
        else {
            //Display error, account creation failed.
            mErrorProperty.setValue("Account username taken.");
        }
    }
}
