package client.ui.HomeView;

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

    public HomeViewModel() {
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
    }

    public Command getShowDraggableViewCommand(){
        return mShowDraggableViewCommand;
    }

    public Command getShowCardDetailViewCommand() {
        return mShowCardDetailViewCommand;
    }
}
