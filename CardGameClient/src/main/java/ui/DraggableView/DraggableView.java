package ui.DraggableView;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class DraggableView implements FxmlView<DraggableViewModel>, Initializable {
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

    @InjectViewModel
    private DraggableViewModel mViewModel;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        mDraggableText.textProperty().bind(mViewModel.draggableTextProperty());

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

    @FXML
    public void navigatePreviousAction() {
        mViewModel.getNavigatePreviousCommand().execute();
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
}
