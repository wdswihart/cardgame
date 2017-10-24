package core;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import javax.print.URIException;
import java.net.URISyntaxException;

public class ConnectionProvider {
    // FIELDS:

    private static Socket mClient;

    // CONSTRUCTORS:

    public ConnectionProvider(String hostname, int port) {
        IO.Options opts = new IO.Options();
        opts.host = hostname;
        opts.port = port;

        try {
            mClient = IO.socket(hostname, opts);
            mClient.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    mClient.send("hallo");
                    mClient.disconnect();
                }
            });
            mClient.open();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // MAIN:

    public static void main(String[] args) {
        ConnectionProvider connectionProvider = new ConnectionProvider("127.0.0.1", 8300);
    }
}
