import java.awt.Color;

public class Particle implements Comparable<Particle>{
	
	
	private static int counter = 1;
	private int ID;
	private Position pos; 
	private Velocity v;	
	private double radio;
	private double masa;
	private Color color;
	
	public Particle(double radio, Color color, double masa, double x, double y, Velocity v) {
		this.pos = new Position(x,y);
		this.v = v;
		this.radio = radio;
		this.color = color;
		this.masa = masa;
		this.ID = counter++;
	}
	
	public Velocity getV() {
		return v;
	}
	
	public double getMasa() {
		return masa;
	}
	
	public Position getPosition(){
		return pos;
	}
	
	
	public double getRadio() {
		return radio;
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
}
