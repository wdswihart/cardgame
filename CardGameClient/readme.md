# Development Basics

The basics behind how to setup your environment and use the beginning frameworks are described here. 

## Environment Setup
I am currently using **IntelliJ Ultimate**, you are free to use eclipse, but I am strongly avoiding it or any other IDE.

You can download this from the JetBrains site. The community edition is fine if you don't want to get the free student access. 

To open a project, open IntelliJ and click open project. Then select the enclosing folder. This client project would be opened by opening the ``` CardGameClient ``` folder.

---

## MVC Architecture
JavaFX uses the MVC architecture, which has a Model, a View, and a Controller.

### Model
The model holds all of the data. This would be the Card, Deck, etc.
These have not been implemented yet.

### View
The views are essentially that. A GUI. 

This project will develop the views using FXML since it seems to be fairly easy to declare interfaces with.

A simple view with two buttons would look like this:
``` fxml
<HBox prefWidth="500" prefHeight="500"
      fx:controller="ViewController">
      
    <Button fx:id="mButton1" text="Button 1"/>
    <Button fx:id="mButton2" text="Button 2"/>
</Hbox>
```
If this view is created within IntelliJ, other attributes will be included, it has been shortened for the sake of brevity.

If the view is to perform any action, it needs to be connected to a controller.
This is done within the ``` fx:controller="ViewController" ``` line.

### Controller

Controllers control the application flow and modify data as well as handle navigation to other views.

We have specified a controller for the last view to be bound to. This is what it would look like. 

``` java
class ViewController {
    // These buttons are created automatically for you.
    // The @FXML annotation automatically hooks them up to the element with the same fx:id.
    @FXML
    private Button mButton1;
    @FXML
    private Button mButton2;

    //This is called when the FXML view is started up. 
    public void initialize() {
    }
}
```

**Note: The view controllers we are using must be derived from ```BaseViewController```.**

---

## Navigation

I have included some basic navigation convenience methods because the JavaFX navigation stuff is full of boilerplate. 

These are found in the ``` BaseViewController ``` and are unaccessible to derived classes. To use the automatic navigation framework a couple steps must be taken.

#### Inherit your controller from ```BaseViewController```
  - This will require implementing the two abstract methods ``` getFxmlFileMap() ``` and ``` getStageMap() ```
  - These will hold a map to the stages you can navigate to and a list of the fxml resource files for the views.

``` java
class ViewController extends BaseViewController {
    @FXML
    private Button mButton1;
    @FXML
    private Button mButton2;
    
    public void initialize() {
        
    }

    protected Map<String, Stage> getStageMap() {
        return new HashMap<String, Stage>();
    }
    protected Map<String, String> getFxmlFileMap() {
        return new HashMap<String, String>();
    }
};
```

#### Connect the FxmlFiles and Stages to a key.
  - This allows you to use the ``` BaseViewController.navigateTo("StageKey") ``` convenience method.

  - Here is an example of navigating to a "GameView" from our previous view with its controller being named "GameViewController".
``` java
class GameViewController extends BaseViewController {
    //This simply returns a new stage. This logic could go somewhere else, but it allows us to set the title and a few other properties here.
    public static Stage getNewStage() {
        Stage stage = new Stage();
        stage.setTitle("Game View);
        return stage;
    }


    protected Map<String, Stage> getStageMap() {
        return null;
    }
    protected Map<String, String> getFxmlFileMap() {
        return null;
    }
}

class ViewController extends BaseViewController {

    //Create a key, the name doesn't really matter. This is for ease of use.
    public final String GAME_VIEW_STAGE_KEY = "GameViewStageKey";

    @FXML
    private Button mButton1;
    @FXML
    private Button mButton2;
    
    public void initialize() {
        
    }

    protected Map<String, Stage> getStageMap() {
        Map<String, Stage> map = new HashMap<>();
    
        //Add your key and the target stage to the stage map.
        map.put(GAME_VIEW_STAGE_KEY, GameViewController.getNewStage());
    
        return map;
    }
    protected Map<String, String> getFxmlFileMap() {
        Map<String, String> map = new HashMap<>();
        
        //Add your key and the target fxml file to the fxml file map.
        map.put(GAME_VIEW_STAGE_KEY, "./Path/To/GameView.fxml");
        
        return map;
    }
};
```

#### Connect the button to the appropriate navigate command.
``` java
class GameViewController extends BaseViewController {
    public static Stage getNewStage() {
        Stage stage = new Stage();
        stage.setTitle("Game View);
        return stage;
    }


    protected Map<String, Stage> getStageMap() {
        return null;
    }
    protected Map<String, String> getFxmlFileMap() {
        return null;
    }
}

class ViewController extends BaseViewController {

    public final String GAME_VIEW_STAGE_KEY = "GameViewStageKey";
    
    @FXML
    private Button mButton1;
    @FXML
    private Button mButton2;

    public void initialize() {
        //Setup button 1 to navigate to the GameView.
        mButton1.setOnMouseReleased(event -> {
            navigateTo(GAME_VIEW_STAGE_KEY);
        });
        
        //Setup button 2 to navigate to the previous view.
        mButton2.setOnMouseReleased(event -> {
            navigatePrevious();
        });
    }

    protected Map<String, Stage> getStageMap() {
        Map<String, Stage> map = new HashMap<>();
    
        map.put(GAME_VIEW_STAGE_KEY, GameViewController.getNewStage());
    
        return map;
    }
    protected Map<String, String> getFxmlFileMap() {
        Map<String, String> map = new HashMap<>();
        
        map.put(GAME_VIEW_STAGE_KEY, "./Path/To/GameView.fxml");
        
        return map;
    }
};
```