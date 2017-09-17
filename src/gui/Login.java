// Login is a login GUI implementation.
//
// Copyright (C) 2017  William Swihart
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the "Software"),
// to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense,
// and/or sell copies of the Software, and to permit persons to whom the
// Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
// FROM,OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
// IN THE SOFTWARE.

package gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*; // JFrame
import javafx.geometry.*;
import java.util.*;

import com.sun.media.jfxmediaimpl.platform.Platform;

public class Login extends Application implements EventHandler<ActionEvent> {
    // FIELDS:

    private final double RATIO = 0.3; // Ratio of stage to screen size
    private final String VERSION_PATH = ""; // VERSION file path (root)
    private final String VERSION_FILENAME = "VERSION"; // VERSION file name
    private String title;
    private String version;
    private Text titleText;
    private Label usernameLabel;
    private TextField usernameField;
    private Label pwLabel;
    private TextField pwField;
    private Button loginButton;

    // START:
    
    @Override
    public void start(Stage stage) {
        // Init title and version from a file.
        this.loadInfo();

        // Create Layout for login scene.
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(20);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Create title text and add to layout.
        this.titleText = new Text(this.title);
        this.titleText.setFont(Font.font("Papyrus", FontWeight.BOLD, 50));
        grid.add(this.titleText, 0, 0, 2, 1);
        
        // Create the username and password labels and fields,
        // and add them to the layout.
        this.usernameLabel = new Label("Username:");
        this.usernameField = new TextField();
        this.pwLabel = new Label("Password:");
        this.pwField = new TextField();

        grid.add(this.usernameLabel, 0, 1);
        grid.add(this.usernameField, 1, 1);
        grid.add(this.pwLabel, 0, 2);
        grid.add(this.pwField, 1, 2);

        // Create the button and add it to the layout.
        this.loginButton = new Button("Log In");
        this.loginButton.addEventHandler(
            ActionEvent.ACTION, this);
        grid.add(this.loginButton, 2, 4);

        // Get screem dimensions, then create login scene.
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(
            grid, screenSize.getMinX(), screenSize.getMinY());

        // Set the stage size, then the login scene, then show the stage.
        stage.setX(screenSize.getMinX() * this.RATIO);
        stage.setY(screenSize.getMinY() * this.RATIO);
        stage.setWidth(screenSize.getWidth() * this.RATIO);
        stage.setHeight(screenSize.getHeight() * this.RATIO);
        stage.setTitle(this.title + ' ' + this.version);
        stage.setScene(scene);
        stage.show();
    }

    // HANDLERS:

    @Override
    public void handle(ActionEvent evt) {
        //TO-DO (this stuff is placeholder)
        if (this.usernameField.getText().equals("test") &&
        this.pwField.getText().equals("test")) {
            System.out.println("You're in!");
            try {
                Thread.sleep(1500);                
            } catch (InterruptedException e) {
                System.err.println("[WARNING] handle(): Failed to sleep.");
                System.err.println(e);
            }
            ((Node)evt.getSource()).getScene().getWindow().hide(); // Close
            return;
        }
        System.out.println("Bad username or password.");
        //END TO-DO
    }

    // METHODS:

    // loadInfo loads program information from the VERSION file.
    // IN: void
    // OUT: program title and version strings (into this object)
    private void loadInfo() {
        List<String> lines = new ArrayList<String>();

        try {
            lines.addAll(Files.readAllLines(
                FileSystems.getDefault().getPath(
                this.VERSION_PATH, this.VERSION_FILENAME)));
            this.title = lines.get(0);
            this.version = lines.get(1);

            return;
        } catch (IOException e) {
            System.err.println("[WARNING] loadInfo(): IO error occured.");
            System.err.println(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("[WARNING] loadInfo(): Index out of bounds.");
            System.err.println(e);
        } catch (InvalidPathException e) {
            System.err.println("[WARNING] loadInfo(): Invalid path.");
            System.err.println(e);
        }

        this.title = "CardGame";
        this.version = "v?.?";
    }

    // MAIN:

    public static void main(String[] args) {
        launch(args);
    }
}
