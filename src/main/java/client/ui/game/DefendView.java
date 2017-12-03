package client.ui.game;

import client.ui.controls.CardControl;
import com.sun.tools.hat.internal.model.Root;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import models.Card;
import models.responses.GameState;

import java.io.IOException;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.List;

public class DefendView implements FxmlView<DefendViewModel> {
    @InjectViewModel
    private DefendViewModel mViewModel;

    @FXML
    private HBox mAttackersBox;
    @FXML
    private HBox mDefendersBox;
    @FXML
    private ListView<Card> mFieldBox;

    public void initialize() {
        mFieldBox.setCellFactory(GameView::smallCardCellFactory);
        mFieldBox.setItems(FXCollections.observableArrayList(new Card(), new Card()));
        mFieldBox.setItems(mViewModel.getFieldList());
        mViewModel.getAttackerList().addListener(this::attackerListChangeListener);
        mViewModel.getDefenderList().addListener(this::defenderListChangeListener);
        mViewModel.getSelectedFieldCard().bind(mFieldBox.getSelectionModel().selectedItemProperty());
    }

    private void defenderListChangeListener(ListChangeListener.Change<? extends Card> c) {
        Platform.runLater(() -> {
            mDefendersBox.getChildren().setAll(getCardNodes(c.getList()));
        });
    }

    private void attackerListChangeListener(ListChangeListener.Change<? extends Card> c) {
        Platform.runLater(() -> {
            mAttackersBox.getChildren().setAll(getCardNodes(c.getList()));
        });
    }

    public void setFieldBox(List<Card> cards) {
        mFieldBox.setItems(FXCollections.observableList(cards));
    }

    private List<Node> getCardNodes(ObservableList<? extends Card> cards) {
        List<Node> nodes = new ArrayList<>();
        try {
            for (Card card : cards) {
                FXMLLoader loader = null;
                Node view = null;
                if (!card.isDefault()) {
                    loader = new FXMLLoader(getClass().getResource("/client/ui/controls/CardControl.fxml"));
                    view = loader.load();
                    CardControl control = loader.getController();
                    control.setCard(card);
                }
                else {
                    loader = new FXMLLoader(getClass().getResource("/client/ui/game/SelectDefenderControl.fxml"));
                    view = loader.load();
                    SelectDefenderControl controller = loader.getController();
                    controller.setCard(card);
                    controller.setAddCallback(mViewModel.getAddDefenderCallback());
                }

                nodes.add(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    @FXML
    public void defendAction() {
        mViewModel.getDefendCommand().execute();
    }
}
