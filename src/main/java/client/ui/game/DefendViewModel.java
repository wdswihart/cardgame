package client.ui.game;

import client.core.ConnectionProvider;
import client.core.navigation.INavigationProvider;
import client.ui.BaseViewModel;
import com.google.inject.Inject;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Card;
import models.requests.DefendPair;
import models.requests.DefendRequest;
import util.JSONUtils;

public class DefendViewModel extends BaseViewModel {

    private ObservableList<Card> mAttackerList = FXCollections.observableArrayList();
    private ObservableList<Card> mDefenderList = FXCollections.observableArrayList();
    private ObservableList<Card> mFieldList = FXCollections.observableArrayList();

    private Property<Card> mSelectedFieldCard = new SimpleObjectProperty<>(new Card());
    private Command mDefendCommand;

    //A notification on this will be the exit from the dialog.
    private Property<DefendRequest> mDefendRequestProperty = new SimpleObjectProperty<>();

    @Inject
    public DefendViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider) {
        super(connectionProvider, navigationProvider);

        mDefendCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                DefendRequest request = new DefendRequest();

                for (int i = 0; i < mAttackerList.size(); i++) {
                    Card attacker = mAttackerList.get(i);
                    Card defender = mDefenderList.get(i);

                    request.addDefendPair(new DefendPair(attacker, defender));
                }

                System.out.println(JSONUtils.toJson(request));
                mDefendRequestProperty.setValue(request);
            }
        });
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

    public Command getDefendCommand() {
        return mDefendCommand;
    }

    public Property<DefendRequest> getDefendRequestProperty() {
        return mDefendRequestProperty;
    }
}
