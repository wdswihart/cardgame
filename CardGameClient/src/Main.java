import core.BaseViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("./ui/ui.ui.HomeView/ui.ui.HomeView.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/HomeView/HomeView.fxml"));
        Parent root = (Parent)loader.load();

        BaseViewController controller = loader.getController();

        controller.setStage(primaryStage);
        controller.setCurrentRoot(root);

        primaryStage.setTitle("Home View");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
