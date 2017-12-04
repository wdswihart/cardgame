package server.handlers.gameplay;

import models.responses.GameState;
import server.core.gameplay.GameStateMachine;
import util.GuiceUtils;

public class PassTurnEventHandler extends BaseGameplayEventHandler<String> {
    public static PassTurnEventHandler getHandler() {
        return GuiceUtils.getInjector().getInstance(PassTurnEventHandler.class);
    }

    @Override
    protected void handle(String model) {
        getGameStateMachine().fire(GameStateMachine.Trigger.PassMain);
    }

    @Override
    protected GameState.State getValidState() {
        return GameState.State.Main;
    }

    @Override
    protected Class<String> getDataClass() {
        return String.class;
    }
}
