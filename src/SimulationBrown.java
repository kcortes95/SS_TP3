import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SimulationBrown {

	private Grid grid;
	private double Rc;
	private int totalFrames;
	private double intervals;
	private Set<Particle> particles;
	private Map<Particle,Set<Particle>> condition = new TreeMap<>();
	
	public SimulationBrown(Grid grid, double Rc, int totalFrames, double intervals, Set<Particle> particles) {
		this.grid  = grid;
		this.Rc = Rc;
		this.totalFrames = totalFrames;
		this.intervals = intervals;
		this.particles = particles;
	}
	
	public void run(){
		int i = 0;
		while(i<=totalFrames){
			simulate();
		}
	}
	
	private void simulate(){
		for(Particle p : particles){
			condition.clear();
			calculateNeighbours();
			updateParticles();
		}
	}

	private void updateParticles() {
		for(Particle p: particles){
			double totalColitions = updatePosition(p);
			double newAngle = 0;
			p.setAngle(newAngle);
		}
	}
	
	private double updatePosition(Particle p){
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

	private void calculateNeighbours() {
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
	}
	
	private double getDistance(Particle p1, Particle p2){
		return Math.sqrt(Math.pow(p1.getPosition().getX()-p2.getPosition().getX(), 2) + Math.pow(p1.getPosition().getY()-p2.getPosition().getY(), 2))-p1.getRadio()-p2.getRadio();
	}
	
	/*
	 * Devuelve true si las dos particulas se tocan en un punto segun su radio.
	 */
	private boolean particlesTouched(Particle p1, Particle p2){
		//(x-a)**2 + (y-b)**2 = r**2
		//link : https://bytes.com/topic/java/answers/645269-circle-circle-intersection-more
		//calculamos la distancia d entre el centro de las particulas de ambas
		double d = Math.sqrt(Math.abs(p1.getPosition().getX()-p2.getPosition().getX())+Math.abs(p1.getPosition().getY()-p2.getPosition().getY()));
		double d1 = (Math.pow(p1.getRadio(), 2) - Math.pow(p2.getRadio(), 2) + Math.pow(d, 2))/(2*d);
		double h = Math.sqrt(Math.pow(p1.getRadio(), 2)-Math.pow(d1, 2));

		double x3 = p1.getPosition().getX() + ( d1 * ( p2.getPosition().getX() - p1.getPosition().getX() ) ) / d;
		double y3 = p1.getPosition().getY() + ( d1 * ( p2.getPosition().getY() - p1.getPosition().getY() ) ) / d;
		
		double x4_i = x3 + ( h * ( d1 * ( p2.getPosition().getX() -  p1.getPosition().getX() ) ) ) / d;
		double y4_i = y3 - ( h * ( d1 * ( p2.getPosition().getY() -  p1.getPosition().getY() ) ) ) / d;
		
		double x4_ii = x3 - ( h * ( d1 * ( p2.getPosition().getX() -  p1.getPosition().getX() ) ) ) / d;
		double y4_ii = y3 + ( h * ( d1 * ( p2.getPosition().getY() -  p1.getPosition().getY() ) ) ) / d;
	}
	
	private void addToCondition(Particle p1, Particle p2){
		if(!condition.containsKey(p1))
			condition.put(p1, new HashSet<Particle>());
		condition.get(p1).add(p2);
	}
	
}
