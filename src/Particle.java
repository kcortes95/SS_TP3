import java.awt.Color;

public class Particle implements Comparable<Particle>{
	
	
	private static int counter = 1;
	private int ID;
	private Position pos; 
	private Velocity v;	
	private double radius;
	private double mass;
	private Color color;
	
	public Particle(double radius, Color color, double mass, double x, double y, Velocity v) {
		this.pos = new Position(x,y);
		this.v = v;
		this.radius = radius;
		this.color = color;
		this.mass = mass;
		this.ID = counter++;
	}
	
	public Velocity getV() {
		return v;
	}
	
	public double getmass() {
		return mass;
	}
	
	public Position getPosition(){
		return pos;
	}
	
	
	public double getradius() {
		return radius;
	}
	

	public void setColor(Color color) {
		this.color = color;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public Color getColor() {
		return color;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Particle)){
			return false;
		}else{
			Particle particleObj = (Particle) obj;
			return this.ID == particleObj.ID;
		}
	}
	
	public String toString(){
		return "" + ID;
	}

	@Override
	public int compareTo(Particle arg0) {
		return this.ID-arg0.getID();
	}
	
	public void updatePos(double time){
		double x = pos.getX()+v.getXVelocity()*time;
		double y = pos.getY()+v.getYVelocity()*time;
		this.pos = new Position(x,y);
	}
	
	public void setPosition(double x, double y){
		this.pos = new Position(x,y);
	}
	
	public void setAngle(double ang){
		this.v.setAngle(ang);
	}
	
	public void collide(Particle p){
		double fvx1, fvx2, fvy1, fvy2;
		double[] speedDif = {p.v.getXVelocity() - this.v.getXVelocity(), p.v.getYVelocity()- this.v.getYVelocity()};
		double[] posDif = {p.pos.getX() - this.pos.getX() , p.pos.getY() - this.pos.getY()};
		double theta = this.radius + p.radius;
		double sPaux = Math.atan2(speedDif[1], speedDif[0])-Math.atan2(posDif[1], posDif[0]);
		double sP = p.getV().getSpeed() * this.getV().getSpeed() * sPaux;
		double impulse = 2 * this.mass * p.mass *sP / (theta * (this.mass + p.mass));
		double xImpulse, yImpulse;
		xImpulse = impulse * (p.getPosition().getX() - this.getPosition().getX()) / theta;
		yImpulse = impulse * (p.getPosition().getY() - this.getPosition().getY()) / theta;
		fvx1= p.getV().getXVelocity() + xImpulse / p.mass;
		fvy1= p.getV().getYVelocity() + yImpulse / p.mass;
		fvx2= this.getV().getXVelocity() + xImpulse / this.mass;
		fvy2= this.getV().getYVelocity() + yImpulse / this.mass;
		p.getV().setSpeed(Math.sqrt(Math.pow(fvx1, 2)+ Math.pow(fvy1,2)));
		p.getV().setAngle(Math.atan2(fvy1, fvx1));
		this.getV().setSpeed(Math.sqrt(Math.pow(fvx2, 2)+ Math.pow(fvy2,2)));
		this.getV().setAngle(Math.atan2(fvy2, fvx2));
		
		
	}
}
