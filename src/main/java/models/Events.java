package models;

public class Events {
    public static final String PLAYER_JOINED = "PlayerJoined";
    public static final String PLAYER_LIST = "PlayerList";

    public static final String LOGIN = "Login";
    public static final String CREATE_ACCOUNT = "CreateAccount";
    public static final String CHAT = "Chat";

    public static final String START_GAME = "StartGame";
    public static final String UPDATE_GAME = "UpdateGame";

    //Used when someone invites you to a game.
    public static final String INVITE_REQUEST = "InviteRequest";

    public static final String DRAW = "Draw";
    public static final String PLAY_CARD = "PlayCard";
    public static final String PASS_TURN = "PassTurn";
    public static final String ATTACK = "Attack";
    public static final String QUIT_GAME = "QuitGame";
    public static final String ACTIVE_GAMES = "ActiveGames";
    public static final String SPECTATE_GAME = "SpectateGame";
    public static final String GAME_CHAT = "GameChat";
}
