
public class Velocity {

	double xVel, yVel;
	
	public Velocity (double speed, double angle){
		this.xVel = speed * Math.cos(angle);
		this.yVel = speed * Math.sin(angle);
	}
	
	public double getSpeed() {
		return Math.sqrt(xVel*xVel+yVel*yVel);
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
	
}
