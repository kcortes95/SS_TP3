
public class Velocity {

	double speed;
	double xVel, yVel;
	
	public Velocity (double speed, double angle){
		this.speed=speed;
		this.xVel = speed * Math.cos(angle);
		this.yVel = speed * Math.sin(angle);
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getXVelocity(){
		return xVel;
	}
	
	public double getYVelocity(){
		return yVel;
	}

	public void setXVel(double value){
		this.xVel = value;
	}
	
	public void setYVel(double value){
		this.yVel = value;
	}
	
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
}
