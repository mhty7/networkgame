

import java.io.*;
import java.net.*;


/**
 * @author mhty7
 *
 */
public class MyConnection {

	private Socket _s;

	
	
	
	private InputStream is;
	//private InputStreamReader isr;
	//private BufferedReader in;
	
	private OutputStream os;
	//private OutputStreamWriter osw;
	//private PrintWriter out;
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public MyConnection(){
	}
	public MyConnection(Socket s){
		this._s=s;
		
		try {
			os=_s.getOutputStream();
			//osw=new OutputStreamWriter(os);
			//out=new PrintWriter(osw);
			oos=new ObjectOutputStream(os);
			oos.flush();
			
			is = _s.getInputStream();
			//isr = new InputStreamReader(is);
			//in=new BufferedReader(isr);
			ois=new ObjectInputStream(is);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	
	public boolean sendObject(Object obj){
		boolean issuccess=true;
		try {
			oos.writeObject(obj);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return issuccess;
	}
	
	public boolean sendMessage(String msg){
		boolean issuccess=true;
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			
			e.printStackTrace();
			issuccess=false;
		}
		//out.println(msg);
		//out.flush();
		return issuccess;
	}
	
	public Object getMessage(){
		Object msg=null;
		try {
			msg=ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;

	}
	
	public void close(){
		try {
			_s.close();
			is.close();
			//isr.close();
			//in.close();
			os.close();
			oos.close();
			//osw.close();
			//out.close();
			ois.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
