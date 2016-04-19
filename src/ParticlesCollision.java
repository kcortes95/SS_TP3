
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
	
	/*public void collide(){
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
	}*/
	
	public void collide()
	{
	    // get the mtd
		Position delta = new Position(p2.getPosition().getX()-p1.getPosition().getX(), p2.getPosition().getY()-p1.getPosition().getY());
	    float d = (float)Math.sqrt(delta.getX()*delta.getX()+delta.getY()*delta.getY());
	    
	    double aux = (p1.getradius() + p2.getradius()-d)/d;
	    // minimum translation distance to push balls apart after intersecting
	    Position mtd = new Position(delta.getX()*aux,delta.getY()*aux); 


	    // resolve intersection --
	    // inverse mass quantities
	    double im1 = 1 / p1.getMass(); 
	    double im2 = 1 / p2.getMass();
	    
	    mtd.setX(mtd.getX()*(im1 / (im1 + im2)));
	    mtd.setY(mtd.getY()*(im1 / (im1 + im2)));

	    // push-pull them apart based off their mass
	    //p1.setPosition(p1.getPosition().getX()+mtd.getX(), p1.getPosition().getY()+mtd.getY());
	   // p2.setPosition(p2.getPosition().getX()+mtd.getX(), p2.getPosition().getY()+mtd.getY());

	    // impact speed
	    Position v = new Position(p2.getV().getXVelocity()-p1.getV().getXVelocity(), p2.getV().getYVelocity()-p1.getV().getYVelocity());
	    double vn = scalarProduct(v, normalize(mtd));

	    // sphere intersecting but moving away from each other already
	    if (vn > 0.0f) return;

	    // collision impulse
	    double i = (-(2) * vn) / (im1 + im2);
	    Position impulse = new Position(mtd.getX()*i,mtd.getY()*i);

	    // change in momentum
	    p1.getV().setXVel(p1.getV().getXVelocity()+impulse.getX()*im1);
	    p1.getV().setYVel(p1.getV().getYVelocity()+impulse.getY()*im1);
	    p2.getV().setXVel(p2.getV().getXVelocity()+impulse.getX()*im2);
	    p2.getV().setYVel(p2.getV().getYVelocity()+impulse.getY()*im2);

	}
	
	public String toString(){
		String s = "P1\n";
		s+="x: ";
		s+=p1.getPosition().getX();
		s+="\ny: ";
		s+=p1.getPosition().getY();
		s+="\nVx: ";
		s+=p1.getV().getXVelocity();
		s+="\nVy: ";
		s+=p1.getV().getYVelocity();
		s+="\nP2\n";
		s+="x: ";
		s+=p2.getPosition().getX();
		s+="\ny: ";
		s+=p2.getPosition().getY();
		s+="\nVx: ";
		s+=p2.getV().getXVelocity();
		s+="\nVy: ";
		s+=p2.getV().getYVelocity();
		return s;
	}
	
	  public Position normalize(Position p) {
	      Position v2 = new Position(0,0);

	      double length = Math.sqrt( p.getX()*p.getX() + p.getY()*p.getY() );
	      if (length != 0) {
	        v2.setX(p.getX()/length);
	        v2.setY(p.getY()/length);
	      }

	      return v2;
	   }  
	
		private double scalarProduct(Position pos1, Position pos2){
			return pos1.getX()*pos2.getX()+pos1.getY()*pos2.getY();
		}
}
