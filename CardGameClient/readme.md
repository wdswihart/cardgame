# Development Basics

The basics behind how to setup your environment and use the beginning frameworks are described here. 

## Environment Setup
I am currently using **IntelliJ Ultimate**, you are free to use eclipse, but I am strongly avoiding it or any other IDE.

You can download this from the JetBrains site. The community edition is fine if you don't want to get the free student access. 

To open a project, open IntelliJ and click open project. Then select the enclosing folder. This client project would be opened by opening the ``` CardGameClient ``` folder.

IntelliJ also provides dependency resolving via build systems. Currently it is using Maven, but I will migrate this to gradle when I get some time.

---

## Project Structure


---


## MVVM Architecture
JavaFX uses the MVC architecture, which has a Model, a View, and a Controller.

MvvmFX takes the JavaFX MVC architecture and wraps MVVM around it. 

### Model
The model holds all of the data. This would be the Card, Deck, etc.
These have not been implemented yet.

### View
The views are essentially that. A GUI. 

This project will develop the views using FXML since it seems to be fairly easy to declare interfaces with.

A simple view with two buttons would look like this:

``` ExampleView.fxml ```
``` fxml
<HBox prefWidth="500" prefHeight="500"
      fx:controller="ExampleView">
      
    <Button onAction="#button1Action" text="Button 1"/>
    <Button onAction="#button2Action" text="Button 2"/>
</Hbox>
```
If this view is created within IntelliJ, other attributes will be included, it has been shortened for the sake of brevity.

If the view is to perform any action, it needs to be connected to a controller, or code-behind.
This is done within the ``` fx:controller="ExampleView" ``` line.

**Note: It is important for the View.java controller and the View.fxml to be named the same. This is to allow the MvvmFX framework to automate some tasks with binding.**

A controller would look something like this:

```java

class ExampleView implements FxmlView<ExampleViewModel> {
    @InjectViewModel
    ViewModel viewModel;
    
    @FXML
    public void button1Action() {
        
    }
    
    @FXML
    public void button2Action() {
        
    }
}

```

There is really one main take away from the controller
 - Its purpose is to forward everything to the view model via bindings.

Looking at what the controller does currently:

  ``` class View implements FxmlView<ExampleViewModel> ```
  - This tells the MvvmFx framework to associate this code-behind to the view model named ViewModel.
    
  ``` java 
  @InjectViewModel
  private ExampleViewModel viewModel;
  ```
  - This tells the MvvmFx framework the field that the view model should be injected into. (Just think of this as magic at this point. It takes care of creating the ExampleViewModel and placing it in the field so it is available to use after the ExampleView controller is created.)


  ``` java
  @FXML
  public void button1Action() {
      
  }
  ```
  - The ``` @FXML ``` annotation allows you to access this method in the .fxml file. We have bound it to each buttons ``` onAction ``` property and this method will be called when the button is pressed.
  
  Next, we should probably implement the ViewModel to go along with this.

### ViewModel

The purpose of the view model is to model the view. Crazy right? 

Essentially, it should hold a data object (not a gui component) that can represent the data being displayed on the view. 

It also handles the business logic such as navigation or interacting with an api.

A view model to continue our previous view+controller example would look like this:

```java

class ExampleViewModel implements ViewModel {
    
}

```

That's it. Of course its not very useful at this point, but it will be injected into the ExampleView controller when the view is created.

For a useful example, lets bind a button to some object in the view model so we can interact with it there, away from GUI components. 

There is an object in the MvvmFx framework called ``` Command ``` that was created for this purpose. It allows you to see if a task is currently running or able to be executed via properties. (Great for loading indicators/Disabling UI elements)

For brevity, we'll just hook up one button.

```java
class ExampleViewModel implements ViewModel {
    Command button1Command;
    
    public ExampleViewModel() {
        button1Command = new DelegateCommand(() -> new Action(){
            @Override
            public void action() {
                //Do stuff here.
            }
        });
    }
    
    public Command getButton1Command() {
        return button1Command;
    }
}
```

I will not explain constructors, because that is fairly common knowledge. 

Within the constructor, we create our command object and pass it a function that returns an Action. This allows the command to create the action only when it is going to execute it. (Thats why it asks for a function that returns an Action rather than the Action itself.)

A simple getter is also included to access our command from the ExampleView controller.

---

Now we have everything in place to bind our button to this view model. This will require changes only in the ExampleView controller.

```java

class ExampleView implements FxmlView<ExampleViewModel> {
    @InjectViewModel
    ViewModel viewModel;
    
    @FXML
    public void button1Action() {
        viewModel.getButton1Command().execute();
    }
    
    @FXML
    public void button2Action() {}
}

```

Simply call the execute method on the Command property you exposed on your view model.

---

## Navigation

Currently, I have used Guice to inject a ``` NavigationProvider ``` into the base class ``` BaseViewModel ```. 

If you inherit your view models from this, navigation will be very straightforward.

Simply access the BaseViewModel's member ``` mNavigationProvider ``` to access navigation.

```java
class ExampleViewModel extends BaseViewModel {
    
    //This is taken care of in the BaseViewModel.
    //Just shown here for example of what it is. 
    @Inject
    private INavigationProvider mNavigationProvider;
    
    private void methodThatNavigates() {
        mNavigationProvider.navigateTo(SomeOtherViewController.class);
    }
    
    private void methodThatNavigatesBack() {
        mNavigationProvider.navigatePrevious();
    }
}
```

## Testing

Currently you can look in the ``` src/test/ ``` directory and see the navigation tests I have in place for the HomeView. They are meant for a demonstration.

WIP...