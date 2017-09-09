package ui.HomeView;

import ui.CardDetailView.CardDetailController;
import core.BaseViewController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ui.DraggableView.DraggableViewController;

import java.util.HashMap;
import java.util.Map;

public class HomeViewController extends BaseViewController {
    public final String CARD_DETAIL_STAGE_KEY = "CardDetailStageKey";
    public final String DRAGGABLE_STAGE_KEY = "DraggableViewStageKey";

    @FXML
    public Button mCardDetailDemoButton;

    @FXML
    public Button mDraggableDemoButton;

    public void initialize() {

        mCardDetailDemoButton.setOnMouseReleased(event -> {
            navigateTo(CARD_DETAIL_STAGE_KEY);
        });

        mDraggableDemoButton.setOnMouseReleased(event -> {
            navigateTo(DRAGGABLE_STAGE_KEY);
        });
    }

    @Override
    protected Map<String, String> getFxmlFileMap() {
        Map<String, String> map = new HashMap<>();

        map.put(CARD_DETAIL_STAGE_KEY, "../../ui/CardDetailView/CardDetailView.fxml");
        map.put(DRAGGABLE_STAGE_KEY, "../../ui/DraggableView/DraggableView.fxml");

        return map;
    }
}
