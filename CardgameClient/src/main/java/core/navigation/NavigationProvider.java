package core.navigation;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.DraggableView.DraggableView;
import ui.DraggableView.DraggableViewModel;

import java.util.Stack;

public class NavigationProvider implements INavigationProvider {
    private static Stack<Parent> sRootStack = new Stack<>();
    private static Stage sStage;
    private static NavigationProvider sInstance = new NavigationProvider();

    private NavigationProvider() {
    }

    public static void setStage(Stage stage) {
        sStage = stage;
    }

    public static NavigationProvider getInstance() {
        return sInstance;
    }

    @Override
    public boolean navigateTo(Class view) {
        ViewTuple<DraggableView, DraggableViewModel> vm = FluentViewLoader.fxmlView(view).load();

        if (sStage.getScene() != null) {
            //Remember previous view if it existed.
            sRootStack.push(sStage.getScene().getRoot());
            sStage.getScene().setRoot(vm.getView());
        }
        else {
            //Init scene since this is the first time.
            sStage.setScene(new Scene(vm.getView()));
        }

        return true;
    }

    @Override
    public boolean navigatePrevious() {
        if (sRootStack.empty()) {
            System.out.println("No previous view.");
            return false;
        }
        sStage.getScene().setRoot(sRootStack.pop());
        return true;
    }
}
