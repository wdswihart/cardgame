package client.ui.home;

import client.core.ConnectionProvider;
import client.core.GameProvider;
import client.ui.game.GameView;
import models.Player;
import client.ui.login.LoginView;
import com.google.inject.Inject;
import client.ui.BaseViewModel;
import client.core.navigation.INavigationProvider;
import de.saxsys.mvvmfx.utils.commands.Action;
import de.saxsys.mvvmfx.utils.commands.Command;
import de.saxsys.mvvmfx.utils.commands.DelegateCommand;
import client.ui.CardDetailView.CardDetailView;
import client.ui.DraggableView.DraggableView;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import models.responses.GameState;

public class HomeViewModel extends BaseViewModel {
    private GameProvider mGameProvider;

    //region CommandDeclarations
    private Command mShowDraggableViewCommand;
    private Command mShowCardDetailViewCommand;
    private Command mLogoutCommand;
    private Command mAcceptInviteCommand;
    private Command mSendCommand;
    private Command mInviteCommand;
    //endregion

    private Property<Player> mSelectedActiveUserProperty = new SimpleObjectProperty<>();
    private ObjectProperty<ObservableList<Player>> mActiveUsersProperty = new SimpleObjectProperty<>();


    private Property<String> mMessageProperty = new SimpleObjectProperty<>();
    private ObjectProperty<ObservableList<String>> mMessagesListProperty = new SimpleObjectProperty<>();

    private Property<ObservableList<Player>> mPendingInvitesProperty = new SimpleObjectProperty<>();
    private Property<Player> mSelectedInviteProperty = new SimpleObjectProperty<>();
    private Property<ObservableList<GameState>> mActiveGamesProperty = new SimpleObjectProperty<>();
    private Command mSpectateCommand;
    private GameState mTargetGame;

    @Inject
    public HomeViewModel(ConnectionProvider connectionProvider,
                         INavigationProvider navigationProvider,
                         GameProvider gameProvider) {
        super(connectionProvider, navigationProvider);
        mGameProvider = gameProvider;


        setupCommands();
        setupProperties();
        setupSubscriptions();
    }

    private void setupSubscriptions() {
        //Logout if we get notified an empty player.
        mConnectionProvider.getAuthenticatedUser().addListener(this::logoutListener);

        //Switch to GameView if we get notified a valid game state.
        mGameProvider.getGameStateProperty().addListener(this::gameStateListener);
    }

    private void setupProperties() {
        mActiveUsersProperty = mConnectionProvider.getActiveUsers();
        mMessagesListProperty = mConnectionProvider.getMessages();
        mPendingInvitesProperty = mGameProvider.getPendingInvitesProperty();
        mActiveGamesProperty = mGameProvider.getActiveGames();
    }

    private void setupCommands() {
        mShowCardDetailViewCommand = new DelegateCommand(() -> new Action() {
            @Override
            public void action() {
                mNavigationProvider.navigateTo(CardDetailView.class);
            }
        });

        mShowDraggableViewCommand = new DelegateCommand(() -> new Action() {
            @Override
            public void action() {
                mNavigationProvider.navigateTo(DraggableView.class);
            }
        });

        mLogoutCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                mConnectionProvider.logoutUser();
            }
        });

        mSendCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                if (mMessageProperty.getValue().isEmpty()) {
                    return;
                }
                mConnectionProvider.sendMessage(mMessageProperty.getValue());
                mMessageProperty.setValue("");
            }
        });

        mInviteCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                if (mSelectedActiveUserProperty.getValue().isDefault() || mSelectedActiveUserProperty.getValue().getUsername().equals(getActiveUsername())) {
                    return;
                }

                mGameProvider.joinGame(mSelectedActiveUserProperty.getValue());
            }
        });

        mAcceptInviteCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                if (mSelectedInviteProperty.getValue().isDefault()) {
                    return;
                }

                mGameProvider.joinGame(mSelectedInviteProperty.getValue());
            }
        });

        mSpectateCommand = new DelegateCommand(() -> new Action() {
            @Override
            protected void action() throws Exception {
                mGameProvider.spectateGame(mTargetGame);
            }
        });
    }

    public ObjectProperty<ObservableList<Player>> getActiveUserProperty() {
        return mActiveUsersProperty;
    }

    public Property<Player> getSelectedActiveUserProperty() {
        return mSelectedActiveUserProperty;
    }

    public Command getShowDraggableViewCommand(){
        return mShowDraggableViewCommand;
    }

    public Command getShowCardDetailViewCommand() {
        return mShowCardDetailViewCommand;
    }

    public Command getLogoutCommand() { return mLogoutCommand; }

    public Command getSendCommand() {
        return mSendCommand;
    }

    public Command getInviteCommand() {
        return mInviteCommand;
    }

    public String getActiveUsername() {
        return mConnectionProvider.getAuthenticatedUser().getValue().getUsername();
    }


    public Property<String> getMessageProperty() {
        return mMessageProperty;
    }

    public ObservableValue<ObservableList<String>> getMessagesListProperty() {
        return mMessagesListProperty;
    }

    public ObservableValue<ObservableList<Player>> getPendingInvitesProperty() {
        return mPendingInvitesProperty;
    }

    private void logoutListener(Observable observable, Player oldPlayer, Player newPlayer) {
        if (newPlayer.isDefault()) {
            mNavigationProvider.navigateTo(LoginView.class);

            //Remove the listener if we ever do actually logout.
            mConnectionProvider.getAuthenticatedUser().removeListener(this::logoutListener);
        }
    }

    private void gameStateListener(Observable observable, GameState oldGameState, GameState newGameState) {
        if (!newGameState.isDefault()) {
            mNavigationProvider.navigateTo(GameView.class);

            //Remove the listener if we do go into the GameView.
            mGameProvider.getGameStateProperty().removeListener(this::gameStateListener);
        }
    }

    public Property<Player> getSelectedInviteProperty() {
        return mSelectedInviteProperty;
    }

    public Command getAcceptInviteCommand() {
        return mAcceptInviteCommand;
    }

    public Property<ObservableList<GameState>> getActiveGamesProperty() {
        return mActiveGamesProperty;
    }

    public Command getSpectateCommand() {
        return mSpectateCommand;
    }

    public void setTargetGame(GameState targetGame) {
        this.mTargetGame = targetGame;
    }
}
