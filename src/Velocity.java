
public class Velocity {

	double speed;
	double angle;
	
	public Velocity (double speed, double angle){
		this.speed=speed;
		this.angle= angle;
	}
	
	public double getAngle() {
		return angle;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getXVelocity(){
		return speed* Math.cos(angle);
	}
	
	public double getYVelocity(){
		return speed* Math.sin(angle);
	}
	
	public void setAngle(double ang){
		this.angle = ang;
	}
	
}
