package core;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public abstract class BaseViewController {
    private Stage mCurrentStage;
    private Stage mPreviousStage;

    private Map<String, Stage> mStageMap;// = new HashMap<>();
    private Map<String, String> mFxmlFileMap;// = new HashMap<>();

    public void setPreviousStage(Stage stage) {
        mPreviousStage = stage;
    }
    public void setCurrentStage(Stage stage) {
        mCurrentStage = stage;
    }

    protected void navigatePrevious() {
        navigateTo(mPreviousStage);
    }

    protected void navigateTo(String stageKey) {
        if (mStageMap == null) {
            mStageMap = getStageMap();
        }
        if (mFxmlFileMap == null) {
            mFxmlFileMap = getFxmlFileMap();
        }

        if (!mStageMap.containsKey(stageKey) || !mFxmlFileMap.containsKey(stageKey)) {
            return;
        }
        navigateTo(mStageMap.get(stageKey), mFxmlFileMap.get(stageKey));
    }

    private void navigateTo(Stage stage, String fxmlFilePath) {
        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFilePath));
            root = (Parent)loader.load();
            BaseViewController controller = loader.getController();
            controller.setPreviousStage(mCurrentStage);
            controller.setCurrentStage(stage);
        } catch (IOException e) {
            System.out.println("Error loading FXML file: " + fxmlFilePath);
            e.printStackTrace();
            System.out.println(e.getMessage());
            return;
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);

        navigateTo(stage);
    }

    private void navigateTo(Stage stage) {
        if (stage == null) {
            return;
        }

        mCurrentStage.close();
        stage.show();
    }

    protected abstract Map<String, Stage> getStageMap();
    protected abstract Map<String, String> getFxmlFileMap();
}
