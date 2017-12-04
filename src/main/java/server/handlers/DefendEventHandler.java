package server.handlers;

import models.requests.DefendRequest;
import models.responses.GameState;
import server.core.gameplay.GameStateMachine;
import server.handlers.gameplay.AttackEventHandler;
import server.handlers.gameplay.BaseGameplayEventHandler;
import util.GuiceUtils;

public class DefendEventHandler extends BaseGameplayEventHandler<DefendRequest> {

    @Override
    public void handle(DefendRequest model) {
        getGameStateMachine().fireWith(GameStateMachine.Trigger.Defend, model);
    }

    @Override
    protected GameState.State getValidState() {
        return GameState.State.Defend;
    }

    public static BaseEventHandler<?> getHandler() {
        return GuiceUtils.getInjector().getInstance(DefendEventHandler.class);
    }

    @Override
    protected Class<DefendRequest> getDataClass() {
        return DefendRequest.class;
    }
}
