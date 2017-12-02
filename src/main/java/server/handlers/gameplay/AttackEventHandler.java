package server.handlers.gameplay;

import models.requests.AttackRequest;
import models.responses.GameState;
import server.core.gameplay.GameStateMachine;
import server.handlers.BaseEventHandler;
import util.GuiceUtils;

public class AttackEventHandler extends BaseGameplayEventHandler<AttackRequest>{
    public static BaseEventHandler<?> getHandler() {
        return GuiceUtils.getInjector().getInstance(AttackEventHandler.class);
    }

    @Override
    protected void handle(AttackRequest model) {
        getGameStateMachine().fireWith(GameStateMachine.Trigger.Attack, model);
    }

    @Override
    protected GameState.State getValidState() {
        return GameState.State.Main;
    }

    @Override
    protected Class<AttackRequest> getDataClass() {
        return AttackRequest.class;
    }
}
