
public class ParticlesCollision extends Collision{

	Particle p1, p2;
	
	public ParticlesCollision(Particle p1, Particle p2,double time) {
		super(time);
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	/*public void collide() {
		double fvx1, fvx2, fvy1, fvy2;
		double[] speedDif = {p1.getV().getXVelocity() - p2.getV().getXVelocity(), p1.getV().getYVelocity()- p2.v.getYVelocity()};
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
	}*/
	
	public void collide(){
		double sigma = p1.getradius() + p2.getradius();
		Position r = new Position(p2.getPosition().getX()-p1.getPosition().getX(), p2.getPosition().getY()-p1.getPosition().getY());
		Position v = new Position(p2.getV().getXVelocity()-p1.getV().getXVelocity(), p2.getV().getYVelocity()-p1.getV().getYVelocity());
		double scalarProduct = r.getX()*v.getX()+r.getY()*v.getY();
		double J = (2*p1.getMass()*p2.getMass()*scalarProduct)/(sigma*(p1.getMass()+p2.getMass()));
		double Jx = J*(p2.getPosition().getX()-p1.getPosition().getX())/sigma;
		double Jy = J*(p2.getPosition().getY()-p1.getPosition().getY())/sigma;
		p1.getV().setXVel(p1.getV().getXVelocity()+Jx/p1.getMass());
		p1.getV().setYVel(p1.getV().getYVelocity()+Jy/p1.getMass());
		p2.getV().setXVel(p2.getV().getXVelocity()+Jx/p2.getMass());
		p2.getV().setYVel(p2.getV().getYVelocity()+Jy/p2.getMass());
	}
	
}
