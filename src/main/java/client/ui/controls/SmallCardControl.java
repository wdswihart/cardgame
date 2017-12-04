package client.ui.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import models.Card;

public class SmallCardControl {
    @FXML
    public Text mName;
    @FXML
    public Text mPower;
    @FXML
    public Text mToughness;

    private Property<Card> mCardProperty = new SimpleObjectProperty<Card>();

    public void setCard(Card card) {
        mName.setText(card.getName());
        mPower.setText(String.valueOf(card.getPower()));
        mToughness.setText(String.valueOf(card.getToughness()));
    }
}
