package core;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;

public class GameStateMachine {
    private StateMachine<State, Trigger> mStateMachine;
    public enum State {
        Refresh,
        Maintain,
        Draw,
        Main,
        Attackers,
        Defenders,
        Damage,
        End
    }
    public enum Trigger {
        MainAttack,
        Attack,
        Damage,
        Defend,
        Draw,
        MainPass,
        Maintain,
        Refresh
    }

    public GameStateMachine() {
        StateMachineConfig<State, Trigger> config = new StateMachineConfig<>();
        config.configure(State.Refresh)
                .permit(Trigger.Refresh, State.Maintain);
        config.configure(State.Maintain)
                .permit(Trigger.Maintain, State.Draw);
        config.configure(State.Draw)
                .permit(Trigger.Draw, State.Main);
        config.configure(State.Main)
                .permit(Trigger.MainAttack, State.Attackers)
                .permit(Trigger.MainPass, State.End);
        config.configure(State.Attackers)
                .permit(Trigger.Attack, State.Defenders);
        config.configure(State.Defenders)
                .permit(Trigger.Defend, State.Damage);
        config.configure(State.Damage)
                .permit(Trigger.Damage, State.Main);
        mStateMachine = new StateMachine<State, Trigger>(State.Draw, config);
    }

    public State getState() {
        return mStateMachine.getState();
    }

    public void fire(Trigger t) {
        mStateMachine.fire(t);
    }
}
