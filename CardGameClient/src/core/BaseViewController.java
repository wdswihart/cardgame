package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public abstract class BaseViewController {
    private Parent mCurrentRoot;
    private Parent mPreviousRoot;

    private Stage mStage;

    private Map<String, String> mFxmlFileMap;

    public void setPreviousRoot(Parent root) {
        mPreviousRoot = root;
    }
    public void setCurrentRoot(Parent root) {
        mCurrentRoot = root;
    }
    public void setStage(Stage stage) {
        mStage = stage;
    }

    protected void navigatePrevious() {
        if (mPreviousRoot == null) {
            return;
        }
        mStage.getScene().setRoot(mPreviousRoot);
        mCurrentRoot = mPreviousRoot;
        mPreviousRoot = null;
    }

    protected void navigateTo(String stageKey) {
        if (mFxmlFileMap == null) {
            mFxmlFileMap = getFxmlFileMap();
        }
        if (mFxmlFileMap == null || !mFxmlFileMap.containsKey(stageKey)) {
            return;
        }
        navigateToFxmlView(mFxmlFileMap.get(stageKey));
    }

    private void navigateToFxmlView(String fxmlFilePath) {
        Parent root = null;
        BaseViewController controller = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            root = (Parent)loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            System.out.println("Error loading FXML file: " + fxmlFilePath);
            e.printStackTrace();
            System.out.println(e.getMessage());
            return;
        }

        //Handles navigation data.
        controller.setPreviousRoot(mCurrentRoot);
        controller.setCurrentRoot(root);
        controller.setStage(mStage);

        mStage.getScene().setRoot(root);
    }

    protected abstract Map<String, String> getFxmlFileMap();
}
