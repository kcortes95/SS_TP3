
public abstract class Collision {
	private double time;
	
	public Collision(double time){
		this.time = time;
	}
	
	public abstract void collide();
	
	public double getTime(){
		return time;
	}
}
