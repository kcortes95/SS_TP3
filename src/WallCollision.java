
public class WallCollision extends Collision{

	Particle p;
	Position direction;
	
	public WallCollision(Particle p, Position direction, double time){
		super(time);
		this.p = p;
		this.direction = direction;
	}
	
	@Override
	public void collide() {
		p.getV().setXVel(p.getV().getXVelocity()*direction.getX());
		p.getV().setYVel(p.getV().getYVelocity()*direction.getY());
	}

}
