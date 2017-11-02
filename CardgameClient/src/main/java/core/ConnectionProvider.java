package core;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.net.URI;
import java.net.URISyntaxException;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.util.Scanner;

public class ConnectionProvider {
    // FIELDS:

    private static Socket mSocket;

    // CLASSES:

    public class Events {
        public static final String PLAYER_JOINED = "PlayerJoined";
        public static final String CHAT = "Chat";
    }

    // CONSTRUCTORS:

    public ConnectionProvider(String hostname, int port) {
        IO.Options opts = new IO.Options();
        opts.host = hostname;
        opts.port = port;
        
        opts.forceNew = true; //DEBUG
        opts.reconnection = false; //DEBUG

        try {
            mSocket = IO.socket("http://" + hostname + ':' + Integer.toString(port));
            mSocket.on(Events.PLAYER_JOINED, (obj) -> {
                System.out.println(obj.toString());
            }).on(Events.CHAT, (obj) -> {
                System.out.println(obj.toString());
            });
            mSocket.connect();            
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    // MAIN:

    public static void main(String[] args) {
        ConnectionProvider connectionProvider = new ConnectionProvider("127.0.0.1", 8300);

        Scanner cin = new Scanner(System.in);        

        while (1 == 1) {
            mSocket.emit(Events.CHAT, "Will: " + cin.nextLine());
        }
    }
}
