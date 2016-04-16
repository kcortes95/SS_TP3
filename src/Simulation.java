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
	
	/**
	 * 
	 * @param grid
	 * @param totalTime - Total simulation runtime in seconds
	 */
	public Simulation(Grid grid, double totalTime){
		if(totalTime<0)
			throw new IllegalArgumentException("Invalid time parameters");
		this.grid = grid;
		this.brute = true;
		this.totalTime = totalTime;
		this.particles = grid.getParticles();
	}
	
	
	public void run(){
		int count = 0;
		double tc = 0;
		double time = 0;
		while(time<=totalTime){
			System.out.println("time: " + time);
			/*if(count++ >= bruteForceRuns){
				brute = false;
				averageTc = time/bruteForceRuns;
			}*/
			calculateTc();
			simulate(nextCol.getTime());
			Output.getInstace().write(particles,time);
			time += nextCol.getTime();
		}
	}
	
	private void simulate(double time){
		for(Particle p: particles){
			if(!brute)
				updateCell(p);
			p.updatePos(time);
		}
		nextCol.collide();
		//calculateNeighbours();
		//updateParticles();
	}
	
	private void calculateTc(){
		//if(brute)
			getBruteTc();
		//else
		//	getApproxTc();
	}
	
	private void getBruteTc(){
		Set<Particle> notChecked = new HashSet<>(particles);
		double L = grid.getL();
		Collision first = null;
		for(Particle p: particles){
			// First check for collisions with box
			Collision localMin = null;
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
			// Then check for collision with other particles
			for(Particle p2: notChecked){
				if(!p2.equals(p)){
					Double crashTime = getCrashTime(p,p2);
					if(localMin!=null && crashTime!=null && crashTime<localMin.getTime()){
						localMin = new ParticlesCollision(p, p2, crashTime);
					}
				}
			}
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
	
	private double getApproxTc(){
		//TODO
		return 0;
	}
	
	private double scalarProduct(Position pos1, Position pos2){
		return pos1.getX()*pos2.getX()+pos1.getY()*pos2.getY();
	}
	
	/*private void updateParticles(){
		for(Particle p: particles){
			updatePosition(p);
			double avAngle = getAverageAngle(p);
			p.setAngle(avAngle + (Math.random()-0.5)*noiseAmp);
		}
	}*/
	
	
	/*private void calculateNeighbours(){
		int M = grid.getM();
		for(int i=0; i<M; i++){
			for(int j=0; j<M; j++){
				for(Particle p1: grid.getCell(i, j).getParticles()){
					// Checks first for other particles in its same cell
					for(Particle paux: grid.getCell(i, j).getParticles()){
						if(!p1.equals(paux)){
							if(getDistance(p1,paux)<=Rc){
								addToCondition(p1,paux);
							}
						}
					}
					// Then checks for particles in adjacent cells
					for(Cell neighbourCell : grid.getCell(i, j).getNeighbours()){
						for(Particle p2: neighbourCell.getParticles()){
							if(getDistance(p1,p2)<=Rc){
								addToCondition(p1,p2);
								addToCondition(p2,p1);
							}	
						}
					}
				}
			}
		}
	}*/
	
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
	

}
