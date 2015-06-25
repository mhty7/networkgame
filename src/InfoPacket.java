import java.io.Serializable;
public class InfoPacket implements Serializable {
	public float x;
	public float y;
	public float angle;
	public int point;
	public String name;
	
	public boolean bflg=false;
	public float bx;
	public float by;
	public float bangle;
	public int bi;
	public int bname;
	
	public boolean eflg=false;
	public float[] eangle;
	public float[] ex;
	public float[] ey;
	public float[] evelocity;
	public float[] eturnAngle;
	public float[] esize;
	
	public int hitenemy;
	public int hitname;
	
	public boolean pflg=false;
	public int point1;
	public int point2;
	public InfoPacket() {
		this.hitenemy=-1;
		this.hitname=-1;
		this.point1=-1;
		this.point2=-1;
		
	}
	
}
