
import java.net.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author mhty7
 *
 */
public class MyServer {

	/**
	 * 
	 */
	private static int uNum=1;
	private static int aNum=0;
	
	private int[] points={0,0};
	
	private Hashtable<String,MyConnection> userHashTable;
	private Hashtable<ServerThread,String> threadLookup;
	private Hashtable<String,String> clientsData;
	
	private AnimationThread animationThr;
	
	public MyServer() {
		userHashTable= new Hashtable<String,MyConnection>();
		threadLookup= new Hashtable<ServerThread,String>();
		clientsData=new Hashtable<String,String>();
		animationThr=new AnimationThread(this);
	}
	private synchronized void broadcastObjectExceptMe(Object msg,ServerThread thr){
		String from="";
		String key="";
		if(threadLookup.containsKey(thr)==true){
			from=threadLookup.get(thr);
		}
		Enumeration<String> e = userHashTable.keys();
		while(e.hasMoreElements()){
			key=e.nextElement();
			if(from.equals(key))
				continue;
			
			userHashTable.get(key).sendObject(msg);
		}	
	}
	private synchronized void broadcastObject(Object message){
		for (Enumeration<MyConnection> e = userHashTable.elements(); e.hasMoreElements();)
			e.nextElement().sendObject(message);
	}
	private synchronized void broadcastMessage(String msg,ServerThread thr){
		String sender="";
		if(thr!=null){
			if(threadLookup.containsKey(thr)==true){
				sender = threadLookup.get(thr);
			}
			else{
				sender ="anonymous";
			}
		}
		else{
			sender = "ServerMessage";
		}
		
		for (Enumeration<MyConnection> e = userHashTable.elements(); e.hasMoreElements();)
			e.nextElement().sendMessage(sender+" : "+msg);
	}
	
	private synchronized void broadcastState(String message){
		for (Enumeration<MyConnection> e = userHashTable.elements(); e.hasMoreElements();)
			e.nextElement().sendMessage(message);
	}
	
	private synchronized void whisperMessage(String msg,ServerThread from,String to){
		if(from!=null){
			if(threadLookup.containsKey(from)==true){
				if(to!=null){
					if(userHashTable.containsKey(to)==true){
						//userHashTable.get(to).sendMessage("["+threadLookup.get(from)+" whispers] "+msg);
						userHashTable.get(to).sendMessage(msg);
					}
				}
				else{
					to=threadLookup.get(from);
					userHashTable.get(to).sendMessage("Server message"+" : "+"Invalid command "+msg);
				}
			}
		}
	}
	
	
	//
	public synchronized void notifyObject(Object input, ServerThread sthr) {
		InfoPacket obj=(InfoPacket)input;
		if(obj.hitenemy>=0||obj.hitname>=1){
			points[obj.hitname-1]+=100;
			animationThr.hitEnemiy(obj.hitenemy);
			
			InfoPacket parcel=new InfoPacket();
			parcel.pflg=true;
			parcel.point1=points[0];
			parcel.point2=points[1];
			broadcastObject(parcel);
			
		}
		
		broadcastObjectExceptMe(obj,sthr);
	}
	public synchronized void notifyMessage(String input, ServerThread sthr) {
		
				
		
		String pst;
		Pattern p;
		Matcher m;
		

		
		if(input.charAt(0)==MySetting.INITIAL_PROTOCOL){
			/*
			pst="(^"+MySetting.CHANGE_STATUS_PROTOCOL+")(\\s+)(.+)";
			p=Pattern.compile(pst);
			m=p.matcher(input);
			if(m.find()){
				updateUser(sthr,null,m.group(3));
				return;
			}
			
			
			pst="(^"+MySetting.CHANGE_NAME_PROTOCOL+")(\\s+)([^\\s]+)";
			p=Pattern.compile(pst);
			m=p.matcher(input);
			if(m.find()){
				updateUser(sthr,m.group(3),null);
				return;
			}
			*/
			
			pst="(^"+MySetting.OK_LANG+")(\\s*)";
			p=Pattern.compile(pst);
			m=p.matcher(input);
			if(m.find()){
				determineUser();
				return;
			}
			
			pst="(^"+MySetting.QUIT_PROTOCOL+")(\\s*)";
			p=Pattern.compile(pst);
			m=p.matcher(input);
			if(m.find()){
				removeUser(sthr);
				return;
			}
			

			//whisperMessage(input,sthr,null);
			
		}
		else{
			//broadcastMessage(input,sthr);
			return;
		}
		
	}
	
	private void determineUser() {
		if(++aNum==2){
			broadcastObject(makeInitParcelForEnemies());
			broadcastState(MySetting.gamestart);
			animationThr.start();
			aNum=0;
		}
		
	}
	public void sendParcelAboutEnemies(){
		InfoPacket parcel= new InfoPacket();
		int num=MySetting.ENEMY_NUM;
		parcel.eflg=true;
		parcel.eangle=new float[num];
		parcel.ex=new float[num];
		parcel.ey=new float[num];
		parcel.evelocity=new float[num];
		parcel.eturnAngle=new float[num];
		parcel.esize=new float[num];
        for (int i=0; i<num; i++){
    		parcel.eangle[i]=animationThr.eangle[i];
    		parcel.ex[i]=animationThr.ex[i];
    		parcel.ey[i]=animationThr.ey[i];
    		parcel.evelocity[i]=animationThr.evelocity[i];
    		parcel.eturnAngle[i]=animationThr.eturnAngle[i];
    		parcel.esize[i]=animationThr.esize[i];
        }
        broadcastObject(parcel);
		
	}
	private Object makeInitParcelForEnemies(){
		InfoPacket parcel= new InfoPacket();
		int num=MySetting.ENEMY_NUM;
		parcel.eflg=true;
		parcel.eangle=new float[num];
		parcel.ex=new float[num];
		parcel.ey=new float[num];
		parcel.evelocity=new float[num];
		parcel.eturnAngle=new float[num];
		parcel.esize=new float[num];
        for (int i=0; i<num; i++){
    		parcel.eangle[i]=animationThr.eangle[i];
    		parcel.ex[i]=animationThr.ex[i];
    		parcel.ey[i]=animationThr.ey[i];
    		parcel.evelocity[i]=animationThr.evelocity[i];
    		parcel.eturnAngle[i]=animationThr.eturnAngle[i];
    		parcel.esize[i]=animationThr.esize[i];
        }
		return parcel;
	}
	private synchronized void updateClient(){
		String key="";
		String val="";
		String msg ="";
		Enumeration<String> e = clientsData.keys();
		while(e.hasMoreElements()){
			key=e.nextElement();
			val=clientsData.get(key);
			msg+=key+" - "+val+";";
		}
		broadcastState(MySetting.UPDATE_CLIENT_PROTOCOL+" "+msg);
	}
	private synchronized void existingClients(){
		String key="";
		String msg ="";
		Enumeration<String> e = clientsData.keys();
		while(e.hasMoreElements()){
			key=e.nextElement();
			msg+=key+";";
		}
		broadcastState(MySetting.EXISTINGCLIENTS_CLIENT_PROTOCOL+":"+msg);
	}
	private synchronized boolean addToTable(String uname,MyConnection mc,ServerThread thr,String newStatus){
		boolean b=false;
		if(uname!=null&&mc!=null&&thr!=null){
			if(threadLookup.containsKey(thr)==false){
				if(userHashTable.containsKey(uname)==false){
					if(clientsData.containsKey(uname)==false){
						threadLookup.put(thr, uname);
						userHashTable.put(uname, mc);
						clientsData.put(uname,(newStatus!=null)?newStatus:"");
						b=true;
					}
				}
			}
		}
		return b;
	}
	private synchronized boolean removeFromTable(ServerThread thr,String uname){
		boolean b=false;
		if(thr!=null&&uname!=null){
			if(threadLookup.containsKey(thr)==true){
				if(userHashTable.containsKey(uname)==true){
					if(clientsData.containsKey(uname)==true){
						threadLookup.remove(thr);
						userHashTable.remove(uname);
						clientsData.remove(uname);
						b=true;
					}
				}
			}
		}
		return b;
	}
	
	private synchronized void addUser(String uname,MyConnection mc,ServerThread thr){
		
		if(uname!=null&&mc!=null&&thr!=null){
			if(uNum++>2)
				uNum=1;
			if(addToTable(uname,mc,thr,null)==true){
				whisperMessage("yourname:"+uname,thr,uname);
				existingClients();
				//broadcastMessage(uname,null);
				
				
			}
			else{
				addUser("Player"+uNum,mc,thr);
			}
			
		}
		
	}
	private synchronized void removeUser(ServerThread thr){
		if(thr!=null){
			String uname=threadLookup.get(thr);
			if(removeFromTable(thr,uname)==true){
				thr.halt();
				updateClient();
				//broadcastMessage(uname+" has disconnected",null);
				broadcastState(MySetting.DISCONNECT_CLIENT_PROTOCOL+":"+uname);
				--uNum;
				--aNum;
			}
		}
	}
	
	private synchronized void updateUser(ServerThread thr,String newName,String newStatus){
		if(newName!=null){
			if(userHashTable.containsKey(newName)){
				return;
			}
		}
		
		if(threadLookup.containsKey(thr)==true){
			String name = threadLookup.get(thr);
			String status = clientsData.get(name);
			if(userHashTable.containsKey(name)==true){
				MyConnection tmp= userHashTable.get(name);
				
				if(removeFromTable(thr,name)==true){
					if(addToTable((newName!=null)?newName:name,tmp,thr,(newStatus!=null)?newStatus:status)==true){
						updateClient();
						if(newName!=null){
							broadcastMessage(name+" has changed name to "+newName,null);
							
						}
						else if(newStatus!=null){
							broadcastMessage(name+" has changed status to "+'"'+newStatus+'"',null);
						}
					}
				}
			}
		}
	}
	
	
	private void init(){
		
		try{
			System.out.println("S:Starting server...");
			ServerSocket ssocket = new ServerSocket(8888);
			//System.out.println("S:Waiting for connections to come...");
			
			while(true){
				
				Socket s= ssocket.accept();
				MyConnection mc =new MyConnection(s);
				ServerThread sthr = new ServerThread(mc,this);
				addUser("Player"+uNum,mc,sthr);
				sthr.start();
				
			
			}
		}catch(Exception e){
			System.out.println("S: x_x"+e);
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		MyServer obj=new MyServer();
		obj.init();
	}


	

}
