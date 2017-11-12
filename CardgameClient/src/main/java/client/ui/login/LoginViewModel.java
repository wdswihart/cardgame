package client.ui.login;

import client.core.ConnectionProvider;
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

public class LoginViewModel extends BaseViewModel implements Observer {
    // METHODS:

    private SimpleStringProperty mUsernameProperty = new SimpleStringProperty("");
    private SimpleStringProperty mPasswordProperty = new SimpleStringProperty("");

    private Command mLoginCommand;

    private ConnectionProvider mConnectionProvider;

    // CONSTRUCTORS:

    @Inject
    public LoginViewModel(ConnectionProvider connectionProvider) {
        mConnectionProvider = connectionProvider;
        mConnectionProvider.getCurrentUser().addObserver(this);
        mLoginCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                mConnectionProvider.loginUser(mUsernameProperty.getValue(), mPasswordProperty.getValue());
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        User user = (User)o;

        if (!user.isDefault()) {
            mNavigationProvider.navigateTo(HomeView.class);
        }
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
