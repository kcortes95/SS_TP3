import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Simulation {
	
	private static final int bruteForceRuns = 1000;
	private Grid grid;
	private double totalTime;
	private boolean brute;
	private double averageTc;
	private Set<Particle> particles;
	private Collision nextCol;
	private double initialSpeed;
	
	/**
	 * 
	 * @param grid
	 * @param totalTime - Total simulation runtime in seconds
	 */
	public Simulation(Grid grid, double totalTime, double initialSpeed){
		if(totalTime<0)
			throw new IllegalArgumentException("Invalid time parameters");
		this.grid = grid;
		this.initialSpeed = initialSpeed;
		this.brute = true;
		this.totalTime = totalTime;
		this.particles = grid.getParticles();
	}
	
	
	public void run(){
		int count = 0;
		double time = 0;
		while(time<=totalTime){
			double Ec = 0;
			System.out.println("ENERGY:");
			for(Particle p: particles)
				Ec += 0.5*p.getMass()*p.getV().getSpeed()*p.getV().getSpeed();
			System.out.println(Ec);
			try{
				Thread.sleep(100);
			}catch (Exception e){
				
			}
			if(count%10==0)
				System.out.println("time: " + time);
			if(count++ == bruteForceRuns){
				brute = false;
				averageTc = time/bruteForceRuns;
				grid = new LinearGrid(grid.getL(), (int)Math.floor(grid.getL()/(initialSpeed*averageTc)));
				for(Particle p: particles)
					grid.insert(p);
			}
			calculateTc();
			simulate(nextCol.getTime());
			Output.getInstace().write(particles,time);
			time += nextCol.getTime();
			count++;
		}
	}
	
	private void simulate(double time){
		for(Particle p: particles){
			if(!brute)
				updateCell(p);
			p.updatePos(time);
		}
		nextCol.collide();
	}
	
	private void calculateTc(){
		nextCol = null;
		if(brute)
			getBruteTc();
		else
			getApproxTc();
	}
	
	private void getBruteTc(){
		Set<Particle> notChecked = new HashSet<>(particles);
		
		Collision first = null;
		for(Particle p: particles){
			// First check for collisions with box
			Collision localMin;
			localMin = getWallTc(p);
			// Then check for collision with other particles
			Collision aux = getParticleTc(p,notChecked);
			if(aux!=null && (localMin==null || aux.getTime()<localMin.getTime()))
				localMin = aux;
			notChecked.remove(p);
			if(first==null || (localMin != null && localMin.getTime() < first.getTime()))
				first = localMin;
		}
		nextCol = first;
	}
	
	private Double getCrashTime(Particle p1, Particle p2){
		double sigma = p1.getradius()+p2.getradius();
		Position r = new Position(p2.getPosition().getX()-p1.getPosition().getX(), p2.getPosition().getY()-p1.getPosition().getY());
		Position v = new Position(p2.getV().getXVelocity()-p1.getV().getXVelocity(), p2.getV().getYVelocity()-p1.getV().getYVelocity());
		if(scalarProduct(v, r)>=0)
			return null;
		double d = Math.pow(scalarProduct(v, r), 2) - scalarProduct(v, v)*(scalarProduct(r, r)-sigma*sigma);
		if(d<0)
			return null;
		double ans = -(scalarProduct(v, r)+Math.sqrt(d))/scalarProduct(v, v);
		if(ans>0)
			return ans;
		return null;
	}
	
	private void getApproxTc(){
		Collision first = null;
		for(Particle p: particles){
			// First check for collisions with box
			Collision localMin;
			localMin = getWallTc(p);
			// Then check for collision with other particles
			for(Cell adj: grid.getCell(p).getNeighbours()){
				Collision aux = getParticleTc(p,adj.getParticles());
				if(aux!=null && (localMin==null || aux.getTime()<localMin.getTime()))
					localMin = aux;
			}
			if(first==null || (localMin != null && localMin.getTime() < first.getTime()))
				first = localMin;
		}
		nextCol = first;
	}
	
	private double scalarProduct(Position pos1, Position pos2){
		return pos1.getX()*pos2.getX()+pos1.getY()*pos2.getY();
	}
	
	private void updateCell(Particle p){
		double cellLength = grid.getL()/grid.getM();
		double x = p.getPosition().getX();
		double y = p.getPosition().getY();
		int cellX = (int) Math.floor(x/cellLength);
		int cellY = (int) Math.floor(y/cellLength);
		int newCellX = (int)Math.floor(p.getPosition().getX()/cellLength);
		int newCellY = (int)Math.floor(p.getPosition().getY()/cellLength);
		if(newCellX != cellX ||newCellY != cellY){
			double newX = p.getPosition().getX();
			double newY = p.getPosition().getY();
			grid.getCell(cellX, cellY).getParticles().remove(p);
			if(newCellX < 0){
				newX = p.getPosition().getX() + grid.getL();
			}else if(newCellX >= grid.getM()){
				newX = p.getPosition().getX() - grid.getL();
			}
			if(newCellY < 0){
				newY = p.getPosition().getY() + grid.getL();
			}else if(newCellY >= grid.getM()){
				newY = p.getPosition().getY() - grid.getL();
			}
			
			p.setPosition(newX, newY);
			grid.insert(p);
		}
	}
	
	public WallCollision getWallTc(Particle p){
		WallCollision localMin = null;
		double L = grid.getL();
		if(p.getV().getXVelocity()>0){
			localMin = new WallCollision(p,new Position(-1,1),(L-p.getPosition().getX()-p.getradius())/p.getV().getXVelocity());
		}else if(p.getV().getXVelocity()<0){
			localMin = new WallCollision(p,new Position(-1,1),(p.getradius()-p.getPosition().getX()/(p.getV().getXVelocity()))); 
		}
		if(p.getV().getYVelocity()>0){
			double yCollisionTime = (L-p.getPosition().getY()-p.getradius())/p.getV().getYVelocity();
			if(localMin==null || yCollisionTime<localMin.getTime())
				localMin = new WallCollision(p,new Position(1,-1),yCollisionTime);
		}else if(p.getV().getYVelocity()<0){
			double yCollisionTime = (p.getradius()-p.getPosition().getY()/(p.getV().getYVelocity()));
			if(localMin == null || yCollisionTime<localMin.getTime())
				localMin = new WallCollision(p,new Position(1,-1),yCollisionTime);
		}
		return localMin;
	}
	
	public ParticlesCollision getParticleTc(Particle p, Set<Particle> checkAgainst){
		ParticlesCollision localMin = null;
		for(Particle p2: checkAgainst){
			if(!p2.equals(p)){
				Double crashTime = getCrashTime(p,p2);
				if(crashTime!=null ){
					Position paux = new Position(p.getPosition().getX(),p.getPosition().getY());
					Position p2aux = new Position(p2.getPosition().getX(),p2.getPosition().getY());
					p.updatePos(crashTime);
					p2.updatePos(crashTime);
					if(localMin==null || (crashTime<localMin.getTime() && validPos(p) && validPos(p2))){
								localMin = new ParticlesCollision(p, p2, crashTime);
					}
					p.setPosition(paux.getX(),paux.getY());
					p2.setPosition(p2aux.getX(), p2aux.getY());
				}
			}
		}
		return localMin;
	}
	
	public boolean validPos(Particle p){
		double L = grid.getL();
		return p.getPosition().getX()>=p.getradius()&& p.getPosition().getX()<L-p.getradius() && p.getPosition().getY()>=p.getradius() && p.getPosition().getY()<L-p.getradius();
	}

}
