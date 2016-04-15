import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Simulation {
	
	private Grid grid;
	private double totalTime;
	private double intervals;
	private Set<Particle> particles;
	private Map<Particle,Set<Particle>> condition = new TreeMap<>();
	
	/**
	 * 
	 * @param grid
	 * @param totalTime - Total simulation runtime in seconds
	 * @param intervals - Time in between calculation periods in seconds
	 */
	public Simulation(Grid grid, double totalTime, double intervals){
		if(totalTime<0 || intervals<0 || intervals>totalTime)
			throw new IllegalArgumentException("Invalid time parameters");
		this.grid = grid;
		this.totalTime = totalTime;
		this.intervals = intervals;
		this.particles = grid.getParticles();
	}
	
	
	//returns va
	public double run(){
		double time = 0;
		while(time<=totalTime){
			simulate();
			Output.getInstace().write(particles,time);
			time += intervals;
		}
		return calculateVa();
	}
	
	private void simulate(){
		condition.clear();
		//calculateNeighbours();
		//updateParticles();
	}
	
	/*private void updateParticles(){
		for(Particle p: particles){
			updatePosition(p);
			double avAngle = getAverageAngle(p);
			p.setAngle(avAngle + (Math.random()-0.5)*noiseAmp);
		}
	}*/
	
	private double getAverageAngle(Particle p){
		double totalSin = Math.sin(p.getV().getAngle());
		double totalCos = Math.cos(p.getV().getAngle());
		if(condition.containsKey(p)){
			for(Particle n: condition.get(p)){
				totalSin += Math.sin(n.getV().getAngle());
				totalCos += Math.cos(n.getV().getAngle());
			}
			totalSin /= condition.get(p).size()+1;
			totalCos /= condition.get(p).size()+1;
		}
		return Math.atan2(totalSin,totalCos);
	}
	
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
	
	private void updatePosition(Particle p){
		double cellLength = grid.getL()/grid.getM();
		double x = p.getPosition().getX();
		double y = p.getPosition().getY();
		p.updatePos(intervals);
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
	
	private void addToCondition(Particle p1, Particle p2){
		if(!condition.containsKey(p1))
			condition.put(p1, new HashSet<Particle>());
		condition.get(p1).add(p2);
	}
	
	private boolean areTouched(Particle p1, Particle p2){

		double distance = Math.pow(Math.pow(p1.getPosition().getX() - p2.getPosition().getX(), 2) + Math.pow(p1.getPosition().getY() - p2.getPosition().getY(), 2), 0.5);
		
		if (p2.getradius() >= p1.getradius() && distance <= (p2.getradius() - p1.getradius())) {
			System.out.println("Circle 1 is inside Circle 2.");
		} else if (p1.getradius() >= p2.getradius() && distance <= (p1.getradius() - p2.getradius())) {
			System.out.println("Circle 2 is inside Circle 1.");
		} else if (distance > (p1.getradius() + p2.getradius())) {
			System.out.println("Circle 2 does not overlap Circle 1.");
		} else {
			System.out.println("Circle 2 overlaps Circle 1.");
		}
		
		if(distance > (p1.getradius() + p2.getradius()))
			return false;
		
		return true;
		
	}
	
	private double calculateVa(){
		double xVel = 0;
		double yVel = 0;
		for(Particle p: particles){
			xVel += p.getV().getXVelocity();
			yVel += p.getV().getYVelocity();
		}
		return Math.sqrt(xVel*xVel+yVel*yVel);
	}

}
