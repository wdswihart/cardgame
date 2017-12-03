package client.ui.home;

import models.responses.GameState;

public interface SpectateCallback {
    void call(GameState gameState);
}
