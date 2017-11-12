import client.core.di.ConnectionProviderModule;
import client.ui.login.LoginView;
import com.google.inject.Module;
import client.core.di.NavigationModule;
import client.core.di.SocketIOModule;
import client.core.navigation.NavigationProvider;
import de.saxsys.mvvmfx.guice.MvvmfxGuiceApplication;
import javafx.stage.Stage;
import client.ui.HomeView.HomeView;

import java.util.List;

public class Main extends MvvmfxGuiceApplication {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initGuiceModules(List<Module> modules) throws Exception {
        //Init DI modules. Wow there is no documentation that says to override this.
        modules.add(new NavigationModule());
        modules.add(new SocketIOModule());
        modules.add(new ConnectionProviderModule());
    }

    @Override
    public void startMvvmfx(Stage stage) throws Exception {
        //We have to get control over a stage somehow, this seems okay to me.
        NavigationProvider.setStage(stage);

        stage.setTitle("Card Game");
        stage.setMaximized(true);

        NavigationProvider.getInstance().navigateTo(LoginView.class);
        stage.show();

        new Thread(() -> {
            new server.GameServer("127.0.0.1", 8087).startServer();
        }).run();
    }
}
