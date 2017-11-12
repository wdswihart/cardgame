package client.ui.HomeView;

import client.core.ConnectionProvider;
import com.google.inject.Inject;
import client.ui.BaseViewModel;
import client.core.navigation.INavigationProvider;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import client.ui.CardDetailView.CardDetailView;
import client.ui.DraggableView.DraggableView;

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
}
