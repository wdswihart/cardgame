package di;

import client.core.GameProvider;
import javafx.beans.property.SimpleObjectProperty;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestGameProviderImpl {
    private static GameProvider sInstance;

    public static GameProvider getInstance() {
        if (sInstance == null) {
            sInstance = mock(GameProvider.class);
            when(sInstance.getGameStateProperty()).thenReturn(new SimpleObjectProperty<>());
        }
        return sInstance;
    }
}
