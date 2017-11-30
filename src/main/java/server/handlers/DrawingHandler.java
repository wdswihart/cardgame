// DrawingHandler handles the drawing of cards.

package server.handlers;

import com.corundumstudio.socketio.SocketIOClient;
import util.GuiceUtils;

public class DrawingHandler extends BaseEventHandler<String> {
    @Override
    protected Class<String> getDataClass() {
        return String.class;
    }

    @Override
    protected void handle(SocketIOClient client, String model) {

    }

    public static DrawingHandler getHandler() {
        return GuiceUtils.getInjector().getInstance(DrawingHandler.class);
    }
}
