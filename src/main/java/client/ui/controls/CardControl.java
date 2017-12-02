package client.ui.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import models.Card;

import java.io.IOException;

public class CardControl {

    @FXML
    private Text mName;
    @FXML
    private TextArea mDescription;
    @FXML
    private Text mPower;
    @FXML
    private Text mToughness;

    public CardControl() {
    }

    public void setCard(Card card) {
        mName.setText(card.getName());
        mDescription.setText(card.getDescription());
        mToughness.setText(String.valueOf(card.getToughness()));
        mPower.setText(String.valueOf(card.getPower()));
    }
}
