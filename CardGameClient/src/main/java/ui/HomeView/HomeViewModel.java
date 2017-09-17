package ui.HomeView;

import com.google.inject.Inject;
import core.BaseViewModel;
import core.navigation.INavigationProvider;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import ui.CardDetailView.CardDetailView;
import ui.DraggableView.DraggableView;

public class HomeViewModel extends BaseViewModel {
    @Inject
    private INavigationProvider mNavigationProvider;

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
