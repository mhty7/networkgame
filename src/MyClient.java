
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


import com.sun.opengl.util.Animator;
import com.sun.opengl.util.GLUT;
import com.sun.opengl.util.texture.Texture;

public class MyClient extends GLJPanel implements GLEventListener,MouseListener, MouseMotionListener {
	GLU glu;
	GLUT glut;
	
	private float CAMERA = 0.0f;
	
	private float pi;
	
	

	
    private Animator animator;
	private float prevMouseX;
	private float prevMouseY;

	private boolean isMouseDown;
	private int loopCnt;
	
	private Texture fly_t;
	private Texture background_t;
	private Texture fighter_t;
	private Texture bullet_t;
	private Texture effect_t;
	private Texture l1_t;
	private Texture l2_t;
	private Texture l3_t;
	private Texture l4_t;
	private Texture label1_t;
	private Texture label2_t;
	
	private int enemy_num;
	private EnemyCharacter[] enemies;
	private int bullet_num;
	private BulletCharacter[] bullets;
	private BulletCharacter[] enemy_bullets;
	private int effect_num;
	private EffectCharacter[] effects;
	private PlayerCharacter myplayer;
	private PlayerCharacter opponent;
	private int point1;
	private int point2;
	private long startingTime;
	private long gameDuration;
	private long remainingTime;
	private boolean isGaming;
	private boolean isGameover;
	private Calendar cal;
	private MyConnection mc;
	
	private JButton replay_btn;
	private JButton ready_btn;
	
	
	
	
	private String myname;
	
	private Map<TextAttribute, Object> attributes;
	
	public MyClient(MyConnection mc){
		
		
		addGLEventListener(this);

		setLayout(new BorderLayout());
		this.mc=mc;
		enemy_num=MySetting.ENEMY_NUM;
		bullet_num=MySetting.BULLET_NUM;
		effect_num=MySetting.EFFECT_NUM;
		enemies = new EnemyCharacter[enemy_num];
		bullets=new BulletCharacter[bullet_num];
		enemy_bullets=new BulletCharacter[bullet_num];
		effects=new EffectCharacter[effect_num];
		pi=MySetting.PI;
		prevMouseX=0.0f;
		prevMouseY=0.0f;
		isMouseDown=false;
		isGaming=false;
		isGameover=false;
		startingTime=0;
		gameDuration=MySetting.GAME_DURATION;
		remainingTime=0;
		loopCnt=0;
		point1=0;
		point2=0;
		
		
		attributes = new HashMap<TextAttribute, Object>();
        attributes.put(TextAttribute.FONT, MySetting.FONT);
        attributes.put(TextAttribute.FOREGROUND, Color.WHITE);
		
		
		
		addMouseListener(this);
		addMouseMotionListener(this);
		animator=new Animator(this);
		//startGame();
		initButtons();
		
		
		
	}
	
	private void toggleButtons() {
		
		if(isGaming){
	    	//replay_btn.setVisible(false);
	    	ready_btn.setVisible(false);
	    }
	    else{
	    	//if(isGameover){
	    		//replay_btn.setVisible(true);
	    	//}
	    	//else{
	    		ready_btn.setVisible(true);
	    	//}
	    }
		//replay_btn.repaint();
    	ready_btn.repaint();
		
	}

	private void initButtons(){
		
		
		ImageIcon btn_ready = new ImageIcon(MySetting.MAT_PATH+"btn_ready.png");
		//ImageIcon btn_replay = new ImageIcon(MySetting.MAT_PATH+"btn_replay.png");
		//replay_btn=new JButton(btn_replay);
		ready_btn=new JButton(btn_ready);
		//replay_btn.setOpaque(false);
		ready_btn.setOpaque(false);
		//replay_btn.setContentAreaFilled(false);
		
		ready_btn.setContentAreaFilled(false);
		ready_btn.setBorderPainted(false);
		ready_btn.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		ready_btn.setPreferredSize(new Dimension(79, 37));
		
		
		//replay_btn.setBorderPainted(false);
		//replay_btn.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		//replay_btn.setAlignmentX(0.5f);
		//replay_btn.setAlignmentY(0.5f);
		ready_btn.setAlignmentX(0.5f);
		ready_btn.setAlignmentY(0.5f);
		ready_btn.setVisible(false);
		
		ready_btn.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					ready_btn.setVisible(false);
					sendMessage(MySetting.OK_LANG);
					
				}
			}
		);
		
		
		JPanel jp=new JPanel();
		jp.setOpaque(false);
		jp.setLayout(new GridBagLayout());
		jp.add(ready_btn,new GridBagConstraints());
		//jp.add(replay_btn,BorderLayout.CENTER);
		
		add(jp,BorderLayout.CENTER);
		//add(replay_btn,BorderLayout.CENTER);
	}
	public void objectReceiver(Object msg){
		
		InfoPacket obj=(InfoPacket)msg;
		if(obj.eflg){
			disposeEnemies(obj.eangle,obj.ex,obj.ey,obj.evelocity,obj.eturnAngle,obj.esize);
		}
		else if(obj.pflg){
			pointsCount(obj.point1,obj.point2);
		}
		else{
			placeOpponent(obj.x,obj.y,obj.angle);
			
			if(obj.bflg){
				disposeBullets(obj.bx,obj.by,obj.bname,obj.bangle,obj.bi);
			}
		}
	}
	private void pointsCount(int p1,int p2){
		if(p1>0){
			point1=p1;
		}
		if(p2>0){
			point2=p2;
		}
	}
	private void disposeEnemies(float[] eangle, float[] ex, float[] ey,
			float[] evelocity, float[] eturnAngle, float[] esize) {
		
		for (int i=0; i<enemy_num; i++){
        	if(eangle!=null)
        		enemies[i].angle=eangle[i];
        	if(ex!=null)
        		enemies[i].x=ex[i];
        	if(ey!=null)
        		enemies[i].y=ey[i];
        	if(evelocity!=null)
        		enemies[i].velocity=evelocity[i];
        	if(eturnAngle!=null)
        		enemies[i].turnAngle=eturnAngle[i];
        	if(esize!=null)
        		enemies[i].size=esize[i];
        }
		
	}

	private void disposeBullets(float bx, float by, int bname, float bangle,int bi){
		BulletCharacter e=enemy_bullets[bi];
		if(e.flg==false){
			e.flg=true;
			e.x=bx;
			e.y=by;
			e.name=bname;
			e.angle=bangle;		
		}
		
	}
	private void placeOpponent(float x,float y,float angle){
		
		opponent.x=x;
		opponent.y=y;
		opponent.angle=angle;
		
	}
	public void messageReceiver(String msg){
		System.out.println(msg);
		if(msg.startsWith("yourname")){
			myname=msg.split(":")[1];
			createPlayer(myname);
		}
		else if(msg.startsWith(MySetting.EXISTINGCLIENTS_CLIENT_PROTOCOL)){
			for(String e:msg.split(":")[1].split(";")){
				if(!e.equals(myname)){
					createPlayer(e);
				}
			}
			
		}
		else if(msg.startsWith(MySetting.DISCONNECT_CLIENT_PROTOCOL)){
			removePlayer(msg.split(":")[1]);
			
		}
		else if(msg.startsWith(MySetting.gamestart)){
			startGame();
		}
		
	}
	private void messegeInit(){
		MessageReceiveThread sendthr = new MessageReceiveThread(mc,this);
		sendthr.start();
	}
	public void sendMessage(String msg){
		mc.sendMessage(msg);
		//MessageSendThread recievethr = new MessageSendThread(mc);
		//recievethr.setInput(msg);
		//recievethr.start();
	}
	public void sendObject(Serializable obj){
		mc.sendObject(obj);
	}
	private void removePlayer(String player){
		if(player.equals("Player1")){
			if(!player.equals(myname)){
				opponent=null;
			}
		}
		else if(player.equals("Player2")){
			if(!player.equals(myname)){
				opponent=null;
			}
		}
	}
	private void createPlayer(String player) {
		if(player.equals("Player1")){
			if(player.equals(myname)){
				myplayer=new PlayerCharacter(-0.25f,0.0f,0.1f,90.0f,0.0f,1,255,fighter_t,label1_t);
			}
			else{
				opponent=new PlayerCharacter(-0.25f,0.0f,0.1f,90.0f,0.0f,1,150,fighter_t,label1_t);
			}
		}
		else if(player.equals("Player2")){
			if(player.equals(myname)){
				myplayer=new PlayerCharacter(0.25f,0.0f,0.1f,90.0f,0.0f,2,255,fighter_t,label2_t);
			}
			else{
				opponent=new PlayerCharacter(0.25f,0.0f,0.1f,90.0f,0.0f,2,150,fighter_t,label2_t);
			}
			
		}
		if(myplayer!=null && opponent!=null && !isGaming){
			toggleButtons();
			
		}
		
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D)g;
	    
	    
	    FontRenderContext renderContext = g2d.getFontRenderContext();
	    TextLayout p1_t=null;
	    TextLayout p2_t=null;
	    if(myplayer!=null){
	    	switch (myplayer.name) {
	    		case 1:
	    			p1_t=new TextLayout("Player 1: " + point1,
	    	                attributes, renderContext);
	    			break;
	    		case 2:
	    			p2_t=new TextLayout("Player 2: " + point2,
	    	                attributes, renderContext);
	    			break;
	    		default:
	    			break;
	    	}
	    }
	    if(opponent!=null){
	    	switch (opponent.name) {
	    		case 1:
	    			p1_t=new TextLayout("Player 1: " + point1,
	    	                attributes, renderContext);
	    			break;
	    		case 2:
	    			p2_t=new TextLayout("Player 2: " + point2,
	    	                attributes, renderContext);
	    			break;
	    		default:
	    			break;
	    	}
	    }
	    
	    String str="";
	    if(isGaming){
	    	str="Remaining Time: "+Long.toString(remainingTime/1000);
	    }
	    else{
	    	if(isGameover){
	    		if(point1>point2)
	    			str="Player1 has won.";
	    		else if(point1<point2)
	    			str="Player2 has won.";
	    		else
	    			str="Ended in a draw.";
	    	}
	    	else{
	    		str="Waiting for another player...";
	    	}
	    }
	    TextLayout layout2= new TextLayout(str,attributes,renderContext);
	    
	    Rectangle2D dimension = g2d.getClip().getBounds2D();
	    float height1_1=0.0f;
	    float height1_2=0.0f;
	    if(p1_t!=null){
	    	Rectangle2D bounds1 = p1_t.getBounds();
	    	height1_1 = (float)(dimension.getHeight() - bounds1.getHeight());
	    	
	    }
	    if(p2_t!=null){
	    	Rectangle2D bounds1 = p2_t.getBounds();
	    	height1_2 = (float)(dimension.getHeight() - bounds1.getHeight());
	    	height1_1-=bounds1.getHeight()+10.0f;
	    }
	    
	    Rectangle2D bounds2 = layout2.getBounds();
	    float width2 = (float)((dimension.getWidth() - bounds2.getWidth())*0.5f);
	    float height2 = (float)(bounds2.getHeight()+10.0f);
	    layout2.draw(g2d, width2,height2);
	    if(p1_t!=null)
	    	p1_t.draw(g2d, 10.0f, height1_1);
	    if(p2_t!=null)
	    	p2_t.draw(g2d, 10.0f, height1_2);
	}
	public void addNotify() {
        super.addNotify();
        animator.start();
    }
	public void startGame(){
		
		cal = Calendar.getInstance();
        startingTime = cal.getTimeInMillis();
        remainingTime=gameDuration;
        isGaming=true;
		
	}
	public void init(GLAutoDrawable drawable){
		GL gl=drawable.getGL();
		glu = new GLU();
		glut=new GLUT();
		loadTexture(gl);
		
		
		
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glShadeModel(GL.GL_SMOOTH);
        gl.glEnable(GL.GL_CULL_FACE);

        gl.glEnable(GL.GL_NORMALIZE);

        
        
        EnemyCharacter enemy=null;
        BulletCharacter bullet=null;
        EffectCharacter effect=null;
        for (int i=0; i<enemy_num; i++){
        	
        	float angle=(float)(Math.random()*360);
        	float x=0.8f*(float)Math.cos(angle/180.0f*pi);
        	float y=0.8f*(float)Math.sin(angle/180.0f*pi);
        	
        	float velocity=(float)Math.random()*0.001f+0.001f;
        	float turnAngle=(float)Math.random()*2.0f-1.0f;
        	float size=(float)Math.random()*0.05f+0.05f;
        	enemy=new EnemyCharacter(x,y,size,velocity,angle,turnAngle,fly_t);
        	enemies[i]=enemy;
        }
        for (int i=0; i<bullet_num; i++){
        	float x=0.6f;
        	float y=0.6f;
        	float angle=0.0f;
        	float velocity=0.003f;
        	float size=0.02f;
        	bullet=new BulletCharacter(x,y,size,velocity,angle,bullet_t);
        	bullets[i]=bullet;
        }
        for (int i=0; i<bullet_num; i++){
        	float x=0.6f;
        	float y=0.6f;
        	float angle=0.0f;
        	float velocity=0.003f;
        	float size=0.02f;
        	bullet=new BulletCharacter(x,y,size,velocity,angle,bullet_t);
        	enemy_bullets[i]=bullet;
        }
        for (int i=0; i<effect_num; i++){
        	float x=1.0f;
        	float y=1.0f;
        	float angle=0.0f;
        	float velocity=0.05f;
        	float size=0.05f;
        	effect=new EffectCharacter(x,y,size,velocity,angle,effect_t);
        	effects[i]=effect;
        }
        messegeInit();
        
	}
	
	private void loadTexture(GL gl){
		try {
			fly_t=GraphicsController.loadTexture(gl,MySetting.MAT_PATH+"fly.png");
			background_t=GraphicsController.loadTexture(gl, MySetting.MAT_PATH+"background.png");
			fighter_t=GraphicsController.loadTexture(gl, MySetting.MAT_PATH+"fighter.png");
			bullet_t=GraphicsController.loadTexture(gl, MySetting.MAT_PATH+"bullet.png");
			l1_t=GraphicsController.loadTexture(gl, MySetting.MAT_PATH+"l1.png");
			l2_t=GraphicsController.loadTexture(gl, MySetting.MAT_PATH+"l2.png");
			l3_t=GraphicsController.loadTexture(gl, MySetting.MAT_PATH+"l3.png");
			l4_t=GraphicsController.loadTexture(gl, MySetting.MAT_PATH+"l4.png");
			effect_t=GraphicsController.loadTexture(gl, MySetting.MAT_PATH+"effect.png");
			label1_t=GraphicsController.loadTexture(gl, MySetting.MAT_PATH+"label1.png");
			label2_t=GraphicsController.loadTexture(gl, MySetting.MAT_PATH+"label2.png");
		} catch (GLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void display(GLAutoDrawable drawable){
		update();
		GL gl = drawable.getGL();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		gl.glPushMatrix();
		

		GraphicsController.drawTexture(gl, 0.0f, 0.0f, 1.0f, 1.0f, background_t, 0.0f, 0.0f, 1.0f, 1.0f, 255, 255, 255,255);
		
		
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		for(EffectCharacter e: effects){
			if(e.flg&&e.isAlive()){
				e.draw(gl);
			}
		}
		for(BulletCharacter e: bullets){
			e.draw(gl);
		}
		for(BulletCharacter e: enemy_bullets){
			e.draw(gl);
		}
		for(EnemyCharacter e: enemies){
			e.draw(gl);
		}
		
		
		if(myplayer != null){
			myplayer.draw(gl);
		}
		if(opponent != null){
			opponent.draw(gl);
		}
		GraphicsController.drawTexture(gl, -0.45f, 0.45f, 0.35f, 0.35f, l1_t, 0.0f, 0.0f, 1.0f, 1.0f, 255, 255, 255,255);
		GraphicsController.drawTexture(gl, 0.45f, 0.45f, 0.35f, 0.35f, l2_t, 0.0f, 0.0f, 1.0f, 1.0f, 255, 255, 255,255);
		GraphicsController.drawTexture(gl, -0.4f, -0.45f, 0.35f, 0.35f, l3_t, 0.0f, 0.0f, 1.0f, 1.0f, 255, 255, 255,255);
		GraphicsController.drawTexture(gl, 0.45f, -0.4f, 0.35f, 0.35f, l4_t, 0.0f, 0.0f, 1.0f, 1.0f, 255, 255, 255,255);
		gl.glDisable(GL.GL_BLEND);
		
		gl.glPopMatrix();
		
		
		gl.glFlush();
		
	}
	
	
	private void update() {
		if(!isGaming){
			return;
		}

		
		InfoPacket parcel=new InfoPacket();
		Point b=getMousePosition();
		if(b!=null){
			prevMouseX=(float)b.x;
			prevMouseY=(float)b.y;
		}
		float tmpx=(float)prevMouseX/500.f-0.5f;
		float tmpy=-((float)prevMouseY/500.f-0.5f);
		float distanceX=tmpx-myplayer.x;
		float distanceY=tmpy-myplayer.y;
		
		if(isMouseDown && loopCnt%25==0 && loopCnt<=200){
			for(int i=0; i<bullet_num; i++){
				BulletCharacter e=bullets[i];
				if(e.flg==false){
					e.flg=true;
					e.x=myplayer.getPointingLocation()[0];
					e.y=myplayer.getPointingLocation()[1];
					e.name=myplayer.name;
					e.angle=myplayer.angle;
					
					parcel.bflg=e.flg;
					parcel.bx=e.x;
					parcel.by=e.y;
					parcel.bname=e.name;
					parcel.bangle=e.angle;
					parcel.bi=i;

					break;
				}
			}
		}
		
		myplayer.accel_x+=distanceX*0.005f;
		myplayer.accel_y+=distanceY*0.005f;
		myplayer.angle=(float)Math.atan2(distanceY,distanceX)/pi*180.0f;
		myplayer.move();
		
		//BulletCharacter mergeArray[]=new BulletCharacter[bullets.length + enemy_bullets.length];
        //System.arraycopy(bullets, 0, mergeArray, 0, bullets.length);
        //System.arraycopy(enemy_bullets, 0, mergeArray, bullets.length, enemy_bullets.length);
        
		for(int j=0; j<bullet_num; j++){
			BulletCharacter e=bullets[j];
			if(e.flg==true){
				e.move();
				
				for(int i=0; i<enemy_num; i++){
					EnemyCharacter ee=enemies[i];
					if(ee.isInside(e.x,e.y)){
						//float angle=(float)(Math.random()*360);
			        	//float x=0.8f*(float)Math.cos(angle/180.0f*pi);
			        	//float y=0.8f*(float)Math.sin(angle/180.0f*pi);
						
			        	for(EffectCharacter eee:effects){
			        		
							
							if(eee.flg==false){
								eee.flg=true;
								eee.x=ee.x;
								eee.y=ee.y;
								
								break;
							}
						}
			        	
			        	//ee.x=x;
			        	//ee.y=y;
			        	parcel.hitname=e.name;
			        	parcel.hitenemy=i;
			        	e.x=0.6f;
			        	e.y=0.6f;
			        	e.name=0;
			        	parcel.bflg=true;
			        	parcel.bi=j;
			        	parcel.bname=e.name;
			        	parcel.bx=e.x;
			        	parcel.by=e.y;
			        	parcel.bangle=0.0f;
			        	
			        	break;
					}
				}
				e.warpOut();
			}
		}
		
		
		
		for(int j=0; j<bullet_num; j++){
			BulletCharacter e=enemy_bullets[j];
			if(e.flg==true){
				e.move();
				for(int i=0; i<enemy_num; i++){
					EnemyCharacter ee=enemies[i];
					
					if(ee.isInside(e.x,e.y)){
						
			        	for(EffectCharacter eee:effects){
			        		
							if(eee.flg==false){
								eee.flg=true;
								eee.x=ee.x;
								eee.y=ee.y;
								break;
							}
						}
			        	break;
					}
				}
				e.warpOut();
			}
		}
		
		
		
		cal = Calendar.getInstance();
		remainingTime=gameDuration-cal.getTimeInMillis()+startingTime;
		if(remainingTime<0){
			remainingTime=0;
			isGaming=false;
			isGameover=true;
		}
		
		for(EffectCharacter e:effects){
			e.move();
		}
		
		/*
		for(EnemyCharacter e:enemies){
			if((int)((float)Math.random()*500.0f)==50){
				e.turnAngle=(float)Math.random()*2.0f-1.0f;
			}
			e.angle=e.angle+e.turnAngle;
			e.move();
			e.warp();
		}
		*/
		
		parcel.name=myname;
		parcel.x=myplayer.x;
		parcel.y=myplayer.y;
		parcel.angle=myplayer.angle;
		sendObject(parcel);
		loopCnt++;
		
		
	}
	public void displayChanged(GLAutoDrawable drawable,boolean modeChanged,boolean deviceChanged){
		
	}
	public void reshape(GLAutoDrawable drawable, int x,int y,int w,int h){
		GL gl=drawable.getGL();
		
		gl.glViewport(0, 0, w, h);
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f);
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f,CAMERA);
	}
	public static void main(String[] args) throws Exception{
		System.out.print("Enter Server IP address:");
		BufferedReader input = new BufferedReader (new InputStreamReader (System.in));
		String ip = input.readLine();
		//String ip="localhost";
		System.out.print("Enter Port number:");
		int port = Integer.parseInt(input.readLine());
		//int port = 8888;
		try{
			System.out.println("C:Connecting to server...");
			Socket s=new Socket(ip,port);
			MyConnection mc=new MyConnection(s);
			final MyClient mygame = new MyClient(mc);
			System.out.println("C: Connected");
			
			

			
			
			JFrame frame = new JFrame("game");
			frame.setSize(500,500);
			frame.setLocationRelativeTo(null);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(mygame);
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					mygame.sendMessage(MySetting.QUIT_PROTOCOL);
					System.exit(0);
				}
			});
			frame.setVisible(true);
			
		}catch(Exception e){
			System.out.println("C: x_x" +e);
			e.printStackTrace();
		}
		
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		isMouseDown=true;
		loopCnt=0;
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		isMouseDown=false;
	}
	@Override
	public void mouseDragged(MouseEvent e) {	
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		
	}

}