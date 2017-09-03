package ui.DraggableView;

import core.BaseViewController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class DraggableViewController extends BaseViewController {
    @FXML
    private Button mBackButton;

    @FXML
    private Text mDraggableText;

    @FXML
    private Pane mPane1;
    @FXML
    private Pane mPane2;
    @FXML
    private Pane mPane3;
    @FXML
    private Pane mPane4;

    public static Stage getNewStage() {
        Stage stage = new Stage();
        stage.setTitle("Draggable Demo");
        return stage;
    }


    public void initialize() {
        mBackButton.setOnMouseReleased(event -> {
            navigatePrevious();
        });

        mDraggableText.setOnDragDetected(event -> {
            Dragboard db = mDraggableText.startDragAndDrop(TransferMode.MOVE);

            db.setDragView(mDraggableText.snapshot(null, null));
            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(mDraggableText.getText());
            db.setContent(content);

            event.consume();
        });

        mPane1.setOnDragOver(new AcceptDragOverEventHandler());
        mPane2.setOnDragOver(new AcceptDragOverEventHandler());
        mPane3.setOnDragOver(new AcceptDragOverEventHandler());
        mPane4.setOnDragOver(new AcceptDragOverEventHandler());

        mPane1.setOnDragDropped(new AcceptDragDroppedEventHandler());
        mPane2.setOnDragDropped(new AcceptDragDroppedEventHandler());
        mPane3.setOnDragDropped(new AcceptDragDroppedEventHandler());
        mPane4.setOnDragDropped(new AcceptDragDroppedEventHandler());
    }

    class AcceptDragOverEventHandler implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }
    class AcceptDragDroppedEventHandler implements EventHandler<DragEvent> {
        @Override
        public void handle(DragEvent event) {
            if (event.getX() == 0 && event.getY() == 0) {
                return;
            }

            mDraggableText.setX(event.getSceneX());
            mDraggableText.setY(event.getSceneY());
        }
    }

    @Override
    protected Map<String, Stage> getStageMap() {
        return new HashMap<>();
    }

    @Override
    protected Map<String, String> getFxmlFileMap() {
        return new HashMap<>();
    }
}
