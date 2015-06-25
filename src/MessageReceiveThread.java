import java.lang.Runnable;

/**
 * @author mhty7
 *
 */
public class MessageReceiveThread implements Runnable {
	
	Thread thr;
	MyConnection mc;
	MyClient mcl;
	private final static String _name = "receive";
	
	public MessageReceiveThread(){
	}
	public MessageReceiveThread(MyConnection mc,MyClient mcl){
		this.thr=new Thread(this,_name);
		this.mc=mc;
		this.mcl=mcl;
	}
	
	public void start(){
		thr.start();
	}
	
	public void run(){
		Object serveroutput;
		
		while ((serveroutput=mc.getMessage())!=null) {
			if(serveroutput.getClass().equals(String.class)){
				mcl.messageReceiver((String)serveroutput);
			}
			else{
				mcl.objectReceiver(serveroutput);
			}
			
			
			if (serveroutput.equals(MySetting.goodbye)) {
				break;
			}
		}	
	}
}