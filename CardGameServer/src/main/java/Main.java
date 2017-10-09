
import core.GameServer;

public class Main {
    public static void main(String[] args) {
        (new GameServer("127.0.0.1", 8087)).startServer();
    }
}
