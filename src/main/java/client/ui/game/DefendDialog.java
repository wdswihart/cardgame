package client.ui.game;

import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import de.saxsys.mvvmfx.internal.viewloader.FxmlViewLoader;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import models.Card;

import java.io.IOException;
import java.util.List;

public class DefendDialog extends Dialog {

    //TODO: Include hands in this.
    public DefendDialog(List<Card> attackers, List<Card> field) {
        ViewTuple<DefendView, DefendViewModel> tuple = FluentViewLoader.fxmlView(DefendView.class).load();
        tuple.getViewModel().setAttackerList(FXCollections.observableArrayList(attackers));
        tuple.getViewModel().setFieldList(FXCollections.observableArrayList(field));
        getDialogPane().setContent(tuple.getView());
    }
}
