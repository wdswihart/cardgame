import core.GameStateMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(JUnitPlatform.class)
public class GameStateMachineTest {
    private GameStateMachine mStateMachine;

    @BeforeEach
    public void initTest() {
        mStateMachine = new GameStateMachine();
    }

    @Test
    public void initialState_drawState() {
        assertEquals(mStateMachine.getState(), GameStateMachine.State.Draw);
    }
}
