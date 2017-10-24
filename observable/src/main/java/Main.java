import com.sun.javafx.collections.ObservableIntegerArrayImpl;
import javafx.beans.Observable;
import javafx.collections.ArrayChangeListener;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableIntegerArray;

import java.lang.Integer;

public class Main {
    private static ObservableIntegerArray observables = new ObservableIntegerArrayImpl();

    public static void main(String[] args) {
        observables.addListener((ObservableIntegerArray observableArray, boolean sizeChanged, int fromIndex, int toIndex) -> {
            for (int i = fromIndex; i < toIndex; i++) {
                System.out.println(observableArray.get(i));
            }

            System.out.println("Size Changed: " + sizeChanged);
        });

        // Array operations
        System.out.println("Array Operations:");

        observables.addAll(1);

        System.out.println("\n---");

        observables.set(0, 5);

        System.out.println("\n---");

        observables.addAll(1, 3, 5, 6, 7);
    }
}
