package client.ui.HomeView;

import client.core.ConnectionProvider;
import client.model.User;
import client.ui.login.LoginView;
import com.google.inject.Inject;
import client.ui.BaseViewModel;
import client.core.navigation.INavigationProvider;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import client.ui.CardDetailView.CardDetailView;
import client.ui.DraggableView.DraggableView;
import javafx.beans.Observable;

public class HomeViewModel extends BaseViewModel {
    private Command mShowDraggableViewCommand;
    private Command mShowCardDetailViewCommand;
    private Command mLogoutCommand;

    @Inject
    public HomeViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider) {
        super(connectionProvider, navigationProvider);

        mShowCardDetailViewCommand = new DelegateCommand(() -> new Action() {
            @Override
            public void action() {
                mNavigationProvider.navigateTo(CardDetailView.class);
            }
        });

        mShowDraggableViewCommand = new DelegateCommand(() -> new Action() {
            @Override
            public void action() {
                mNavigationProvider.navigateTo(DraggableView.class);
            }
        });

        //Logout if we get notified an empty user.
        mConnectionProvider.getAuthenticatedUser().addListener(this::logoutListener);
        mLogoutCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                mConnectionProvider.logoutUser();
            }
        });
    }

    public Command getShowDraggableViewCommand(){
        return mShowDraggableViewCommand;
    }

    public Command getShowCardDetailViewCommand() {
        return mShowCardDetailViewCommand;
    }

    public Command getLogoutCommand() { return mLogoutCommand; }

    private void logoutListener(Observable observable, User oldUser, User newUser) {
        if (newUser.isDefault()) {
            mNavigationProvider.navigateTo(LoginView.class);

            //Remove the listener if we ever do actually logout.
            mConnectionProvider.getAuthenticatedUser().removeListener(this::logoutListener);
        }
    }
}
