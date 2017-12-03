package client.ui.game;

import client.core.ConnectionProvider;
import client.core.navigation.INavigationProvider;
import client.ui.BaseViewModel;
import com.google.inject.Inject;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Card;

import java.util.List;

public class DefendViewModel extends BaseViewModel {

    private ObservableList<Card> mAttackerList = FXCollections.observableArrayList();
    private ObservableList<Card> mDefenderList = FXCollections.observableArrayList();
    private ObservableList<Card> mFieldList = FXCollections.observableArrayList();

    private Property<Card> mSelectedFieldCard = new SimpleObjectProperty<>(new Card());

    @Inject
    public DefendViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider) {
        super(connectionProvider, navigationProvider);

    }

    public void setAttackerList(ObservableList<Card> cards) {
        mDefenderList.clear();
        for (Card card : cards) {
            //Add blanks to the defenders to start.
            mDefenderList.add(new Card());
        }

        mAttackerList.setAll(cards);
    }

    public ObservableList<Card> getAttackerList() {
        return mAttackerList;
    }

    public void setFieldList(ObservableList<Card> cards) {
        mFieldList.setAll(cards);
    }

    public ObservableList<Card> getFieldList() {
        return mFieldList;
    }

    public void setDefenderList(ObservableList<Card> cards) {
        mDefenderList.setAll(cards);
    }

    public ObservableList<Card> getDefenderList() {
        return mDefenderList;
    }

    public Property<Card> getSelectedFieldCard() {
        return mSelectedFieldCard;
    }

    public AddDefenderCallback getAddDefenderCallback() {
        return new AddDefenderCallback() {
            @Override
            public void call(Card card) {
                if (mSelectedFieldCard.getValue() == null || mSelectedFieldCard.getValue().isDefault()) {
                    return;
                }

                mDefenderList.set(mDefenderList.indexOf(card), mSelectedFieldCard.getValue());
                mFieldList.remove(mSelectedFieldCard.getValue());
            }
        };
    }
}
