import client.ui.discovery.DiscoveryView;
import client.ui.game.DefendDialog;
import client.ui.login.LoginView;
import com.google.inject.Module;
import client.core.navigation.NavigationProvider;
import de.saxsys.mvvmfx.guice.MvvmfxGuiceApplication;
import di.DependencyModules;
import io.netty.util.internal.EmptyPriorityQueue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.ParallelCamera;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Card;
import server.GameServer;
import util.GuiceUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends MvvmfxGuiceApplication {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                GuiceUtils.getInjector().getInstance(GameServer.class).startServer();
            }
            catch (Exception e) {
                System.out.printf("Server failed to start.");
                e.printStackTrace();
            }
        }).start();
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

        NavigationProvider.getInstance().navigateTo(DiscoveryView.class);
        stage.show();

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/game/DefendView.fxml"));
//        try {
//            Parent node = loader.load();
//            stage.setScene(new Scene(node));
//            stage.show();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
