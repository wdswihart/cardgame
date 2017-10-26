package core;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URI;
import java.net.URISyntaxException;

public class ConnectionProvider {
    // FIELDS:

    private static Socket mSocket;

    // CONSTRUCTORS:

    public ConnectionProvider(String hostname, int port) {
        IO.Options opts = new IO.Options();
        opts.host = hostname;
        opts.port = port;
        
        opts.forceNew = true; //DEBUG
        opts.reconnection = false; //DEBUG

        try {
            mSocket = IO.socket("http://" + hostname + ':' + Integer.toString(port));
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // MAIN:

    public static void main(String[] args) {
        ConnectionProvider connectionProvider = new ConnectionProvider("127.0.0.1", 8300);
    }
}
