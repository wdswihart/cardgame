package client.ui.createaccount;

import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CreateAccountView implements FxmlView<CreateAccountViewModel> {
    @InjectViewModel
    CreateAccountViewModel mViewModel;

    @FXML
    private Text mErrorText;

    @FXML
    private TextField mUsernameField;

    @FXML
    private TextField mPasswordField;

    @FXML
    private TextField mPasswordAgainField;

    public void initialize() {
        mErrorText.textProperty().bind(mViewModel.getErrorProperty());
        mUsernameField.textProperty().bindBidirectional(mViewModel.getUsernameProperty());
        mPasswordField.textProperty().bindBidirectional(mViewModel.getPasswordProperty());
        mPasswordAgainField.textProperty().bindBidirectional(mViewModel.getPasswordAgainProperty());
    }

    @FXML
    private void cancelAction() {
        mViewModel.getNavigatePreviousCommand().execute();
    }

    @FXML
    private void createAction() {
        mViewModel.getCreateAccountCommand().execute();
    }
}
