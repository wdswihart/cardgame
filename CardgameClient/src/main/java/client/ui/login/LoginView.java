package client.ui.login;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
    private void createAccountAction() {}
}
