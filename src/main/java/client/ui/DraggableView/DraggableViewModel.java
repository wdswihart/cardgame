package client.ui.DraggableView;

import client.core.ConnectionProvider;
import client.core.navigation.INavigationProvider;
import client.ui.BaseViewModel;
import com.google.inject.Inject;
import javafx.beans.property.SimpleStringProperty;

public class DraggableViewModel extends BaseViewModel {

    private SimpleStringProperty draggableText = new SimpleStringProperty("Drag Me");

    public SimpleStringProperty draggableTextProperty() {
        return draggableText;
    }

    @Inject
    public DraggableViewModel(ConnectionProvider connectionProvider, INavigationProvider navigationProvider) {
        super(connectionProvider, navigationProvider);
    }

    public String getDraggableText() {
        return draggableText.get();
    }

    public void setDraggableText(String draggableText) {
        this.draggableText.set(draggableText);
    }
}
