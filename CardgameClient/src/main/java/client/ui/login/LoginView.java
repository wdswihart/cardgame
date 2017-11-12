package client.ui.login;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginView implements FxmlView<LoginViewModel> {
    @InjectViewModel
    public LoginViewModel mViewModel;

    public TextField mUsernameField;
    public TextField mPasswordField;
    public Button mLoginButton;

    public void instantiate() {
        mUsernameField.textProperty().bind(mViewModel.getUsernameProperty());
        mPasswordField.textProperty().bind(mViewModel.getPasswordProperty());
    }

    @FXML
    private void loginAction() {
        mViewModel.getLoginCommand().execute();
    }
}
