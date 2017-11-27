package models;

public class Events {
    public static final String PLAYER_JOINED = "PlayerJoined";
    public static final String PLAYER_LIST = "PlayerList";

    public static final String LOGIN = "Login";
    public static final String CREATE_ACCOUNT = "CreateAccount";
    public static final String CHAT = "Chat";

    public static final String START_GAME = "StartGame";

    //Used when someone invites you to a game.
    public static final String INVITE_REQUEST = "InviteRequest";

    public static final String REFRESH_SERVER = "RefreshServer";
    public static final String MAINTAIN_SERVER = "MaintainServer";
    public static final String DRAW_SERVER = "DrawServer";

    public static final String MAIN_SERVER = "MainServer";

    public static final String ATTACK_CLIENT = "AttackClient";
    public static final String DEFEND_CLIENT = "DefendClient";
    public static final String DAMAGE_SERVER = "DamageClient";

    public static final String PLAY_CLIENT = "PlayClient";

    public static final String RESPOND_CLIENT = "RespondClient";
    public static final String RESPOND_SERVER = "RespondServer";
}
