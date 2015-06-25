
public class AnimationThread implements Runnable {
    
    private Thread thr;
    private boolean _halt;
    private MyServer ms;
    
    public float[] eangle;
	public float[] ex;
	public float[] ey;
	public float[] evelocity;
	public float[] eturnAngle;
	public float[] esize;
    private int num;
    private float pi;
    public AnimationThread(MyServer ms) {
    	this.ms=ms;
    	this.thr=new Thread(this);
		this._halt=false;
		num=MySetting.ENEMY_NUM;
		pi=MySetting.PI;
		eangle=new float[num];
		ex=new float[num];
		ey=new float[num];
		evelocity=new float[num];
		eturnAngle=new float[num];
		esize=new float[num];
		initEnemies();
    }
    private void initEnemies(){
		
        for (int i=0; i<num; i++){
        	float angle=(float)(Math.random()*360);
    		eangle[i]=angle;
    		ex[i]=0.8f*(float)Math.cos(angle/180.0f*pi);
    		ey[i]=0.8f*(float)Math.sin(angle/180.0f*pi);
    		evelocity[i]=(float)Math.random()*0.001f+0.001f;
    		eturnAngle[i]=(float)Math.random()*2.0f-1.0f;
    		esize[i]=(float)Math.random()*0.05f+0.05f;
        }
    	
    }
    public void start(){
		thr.start();
		
	}
    public void hitEnemiy(int i){
    	float angle=(float)(Math.random()*360);
    	float x=0.8f*(float)Math.cos(angle/180.0f*pi);
    	float y=0.8f*(float)Math.sin(angle/180.0f*pi);
    	ex[i]=x;
    	ey[i]=y;
    }
    public void run(){
        try {
        	while(true){
        		for(int i=0; i<num; i++){
        			if((int)((float)Math.random()*500.0f)==50){
        				eturnAngle[i]=(float)Math.random()*2.0f-1.0f;
        			}
        			eangle[i]=eangle[i]+eturnAngle[i];
        			float theta = eangle[i]/180.0f*pi;
        			ex[i]=ex[i]+(float)Math.cos(theta)*evelocity[i];
        			ey[i]=ey[i]+(float)Math.sin(theta)*evelocity[i];
        			if(ex[i]>=0.8f)
        				ex[i]-=1.6f;
        			if(ex[i]<=-0.8f)
        				ex[i]+=1.6f;
        			if(ey[i]>=0.8f)
        				ey[i]-=1.6f;
        			if(ey[i]<=-0.8f)
        				ey[i]+=1.6f;
        		}
        		
    			
        		ms.sendParcelAboutEnemies();
        		
        		Thread.sleep(10);
        		if (_halt) {
    				break;
    			}
        	}
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void halt(){
		_halt=true;
	}
}