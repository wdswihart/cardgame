package ui.CardDetailView;

import core.BaseViewModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleListProperty;
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
