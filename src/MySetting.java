

import java.awt.Font;

public class MySetting {

	public MySetting() {
	}
	
	public final static String MAT_PATH = "mat/";
	public final static float PI = 3.14f;
	public final static int ENEMY_NUM = 15;
	public final static int BULLET_NUM = 30;
	public final static int EFFECT_NUM =10;
	public final static Font FONT = new Font("SansSerif", Font.BOLD, 15);
	public final static long GAME_DURATION =60000;
	
	
	
	
	
	//server
	public final static String goodbye="Goodbye.";
	public final static String gamestart="GameStart.";
	
	public enum Type{
		CHAT_WINDOW,ONLINE_CLIENT
	};
	
	public final static char INITIAL_PROTOCOL ='/';
	public final static String UPDATE_CLIENT_PROTOCOL =INITIAL_PROTOCOL+"updateclient";
	
	public final static String CHANGE_STATUS_PROTOCOL =INITIAL_PROTOCOL+"changestatus";
	public final static String WHISPER_PROTOCOL =INITIAL_PROTOCOL+"whisper";
	public final static String CHANGE_NAME_PROTOCOL =INITIAL_PROTOCOL+"changename";
	public final static String QUIT_PROTOCOL =INITIAL_PROTOCOL+"quit";
	
	public final static String EXISTINGCLIENTS_CLIENT_PROTOCOL =INITIAL_PROTOCOL+"existingclients";
	public final static String DISCONNECT_CLIENT_PROTOCOL =INITIAL_PROTOCOL+"disconnect";
	public final static String OK_LANG =INITIAL_PROTOCOL+"OK";
	
}
