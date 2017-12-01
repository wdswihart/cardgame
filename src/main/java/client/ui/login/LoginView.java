package client.ui.login;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class LoginView implements FxmlView<LoginViewModel> {
    @InjectViewModel
    public LoginViewModel mViewModel;

    @FXML
    public Text mErrorText;
    @FXML
    public TextField mUsernameField;
    @FXML
    public TextField mPasswordField;
    @FXML
    public Button mLoginButton;

    public void initialize() {
        mErrorText.textProperty().bind(mViewModel.getErrorProperty());
        mUsernameField.textProperty().bindBidirectional(mViewModel.getUsernameProperty());
        mPasswordField.textProperty().bindBidirectional(mViewModel.getPasswordProperty());
    }

    @FXML
    private void loginAction() {
        mViewModel.getLoginCommand().execute();
    }

    @FXML
    private void createAccountAction() {
        mViewModel.getShowCreateAccountViewCommand().execute();
    }

    public void onKeyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            loginAction();
        }
    }
}
