package core;

import com.google.inject.Inject;
import core.navigation.INavigationProvider;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;

public class BaseViewModel implements ViewModel {
    @Inject
    INavigationProvider mNavigationProvider;

    private Command mNavigatePreviousCommand;

    public BaseViewModel() {
        mNavigatePreviousCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                mNavigationProvider.navigatePrevious();
            }
        });
    }

    public Command getNavigatePreviousCommand() {
        return mNavigatePreviousCommand;
    }
}
