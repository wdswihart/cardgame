import com.google.inject.Module;
import core.di.NavigationModule;
import core.navigation.NavigationProvider;
import de.saxsys.mvvmfx.guice.MvvmfxGuiceApplication;
import javafx.stage.Stage;
import ui.HomeView.HomeView;

import java.util.ArrayList;
import java.util.List;

public class Main extends MvvmfxGuiceApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initGuiceModules(List<Module> modules) throws Exception {
        //Init DI modules. Wow there is no documentation that says to override this.
        modules.add(new NavigationModule());
    }

    @Override
    public void startMvvmfx(Stage stage) throws Exception {
        //We have to get control over a stage somehow, this seems okay to me.
        NavigationProvider.setStage(stage);

        stage.setTitle("Card Game");
        stage.setMaximized(true);

        NavigationProvider.getInstance().navigateTo(HomeView.class);
        stage.show();
    }
}
