package client.ui.CardDetailView;

import client.ui.BaseViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CardDetailViewModel extends BaseViewModel {
    private ObservableList<String> cardListProperty = FXCollections.observableArrayList();


    public CardDetailViewModel() {
        cardListProperty.add("Test1");
        cardListProperty.add("Test2");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
        cardListProperty.add("Test3");
    }

    public ObservableList<String> getCardListProperty() {
        return cardListProperty;
    }

    public void setCardListProperty(ObservableList<String> cardListProperty) {
        this.cardListProperty = cardListProperty;
    }
}
