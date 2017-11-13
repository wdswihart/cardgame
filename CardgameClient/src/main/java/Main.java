import client.ui.login.LoginView;
import com.google.inject.Module;
import client.core.navigation.NavigationProvider;
import de.saxsys.mvvmfx.guice.MvvmfxGuiceApplication;
import di.DependencyModules;
import javafx.stage.Stage;
import server.GameServer;
import util.GuiceUtils;

import java.util.List;

public class Main extends MvvmfxGuiceApplication {

    public static void main(String[] args) {
        new Thread(() -> {
            GuiceUtils.getInjector().getInstance(GameServer.class).startServer();
        }).run();

        launch(args);
    }

    @Override
    public void initGuiceModules(List<Module> modules) throws Exception {
        //Init DI modules. Wow there is no documentation that says to override this.
        modules.add(new DependencyModules());
    }

    @Override
    public void startMvvmfx(Stage stage) throws Exception {
        //We have to get control over a stage somehow, this seems okay to me.
        NavigationProvider.setStage(stage);

        stage.setTitle("Card Game");
        stage.setMaximized(true);

        NavigationProvider.getInstance().navigateTo(LoginView.class);
        stage.show();
    }
}
