package core;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ui.DraggableView.DraggableView;
import ui.DraggableView.DraggableViewModel;

import java.util.Stack;

public abstract class BaseViewController {
    private static Stack<Parent> sRootStack = new Stack<>();
    private static Stage sStage;

    public void setStage(Stage stage) {
        sStage = stage;
    }

    protected void navigatePrevious() {
        if (sRootStack.empty()) {
            System.out.println("No previous view.");
            return;
        }
        sStage.getScene().setRoot(sRootStack.pop());
    }

    protected void navigateTo(Class view) {
        sRootStack.push(sStage.getScene().getRoot());

        ViewTuple<DraggableView, DraggableViewModel> vm = FluentViewLoader.fxmlView(view).load();
        sStage.getScene().setRoot(vm.getView());
    }
}
