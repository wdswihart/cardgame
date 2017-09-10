package ui.DraggableView;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleStringProperty;

public class DraggableViewModel implements ViewModel {
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
