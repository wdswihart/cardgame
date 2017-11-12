package client.ui.DraggableView;

import client.ui.BaseViewModel;
import javafx.beans.property.SimpleStringProperty;

public class DraggableViewModel extends BaseViewModel {

    private SimpleStringProperty draggableText = new SimpleStringProperty("Drag Me");

    public SimpleStringProperty draggableTextProperty() {
        return draggableText;
    }

    public String getDraggableText() {
        return draggableText.get();
    }

    public void setDraggableText(String draggableText) {
        this.draggableText.set(draggableText);
    }
}
