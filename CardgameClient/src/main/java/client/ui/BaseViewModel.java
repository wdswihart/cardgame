package client.ui;

import client.core.ConnectionProvider;
import client.model.User;
import client.ui.login.LoginView;
import com.google.inject.Inject;
import client.core.navigation.INavigationProvider;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;

public class BaseViewModel implements ViewModel {

    protected INavigationProvider mNavigationProvider;

    protected ConnectionProvider mConnectionProvider;


    //#region properties
    protected SimpleStringProperty mErrorProperty = new SimpleStringProperty();

    public SimpleStringProperty getErrorProperty() {
        return mErrorProperty;
    }
    //#endregion

    //#region commands
    private Command mNavigatePreviousCommand;

    public Command getNavigatePreviousCommand() {
        return mNavigatePreviousCommand;
    }
    //#endregion

    @Inject
    public BaseViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider) {
        mConnectionProvider = connectionProvider;
        mNavigationProvider = navigationProvider;

        mNavigatePreviousCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                mNavigationProvider.navigatePrevious();
            }
        });

        //Logout if we get notified an empty user.
        mConnectionProvider.getCurrentUser().addListener(this::logoutListener);
    }

    private void logoutListener(Observable observable, User oldUser, User newUser) {
        if (newUser.isDefault()) {
            mNavigationProvider.navigateTo(LoginView.class);

            //Remove the listener if we ever do actually logout.
            mConnectionProvider.getCurrentUser().removeListener(this::logoutListener);
        }
    }
}
