/**
 * @author mhty7
 *
 */
public class MessageSendThread implements Runnable {

	Thread thr;
	MyConnection mc;
	String userinput;
	private final static String _name = "send";
	
	public MessageSendThread() {
	}
	
	public MessageSendThread(MyConnection mc){
		this.thr=new Thread(this,_name);
		this.mc=mc;
	}
	public void setInput(String ui){
		this.userinput=ui;
	}
	public void start(){
		
		thr.start();
		
	}
	
	public void run(){

		mc.sendMessage(userinput);

	}

}
