import java.awt.Color;
import java.util.HashSet;
import java.util.Set;


public abstract class Grid {
	
	private Set<Particle> particles;
	private Cell[][] cells;
	private double L;
	private int M;
	
	public Grid(double L, int M){
		this.L = L;
		this.M = M;
		cells = new Cell[M][M];
		for(int i=0; i<M; i++){
			for(int j=0; j<M; j++){
				cells[i][j] = new Cell();
			}
		}
		this.particles = new HashSet<>();
		calculateNeighbours();
	}
	
	public abstract void calculateNeighbours();
	
	/**
	 * Inserts N particles into the grid
	 * @param r - radius of particles to be inserted
	 * @param m - mass of the particles to be inserted
	 * @param N - amount of particles to be inserted
	 * @param R - radius of the Big Particle
	 * @param P - position of the Big Particle
	 */
	public void generateParticles(double maxV, double r, double m, int N){
		int total = 0;
		while(total<N){
			boolean collides = false;
			Particle p = new Particle(r, Color.CYAN, m, generateRandomPos(r), new Velocity(maxV,Math.random()*2*Math.PI));
			for(Particle other: particles){
				if(p.getDistance(other)<=0){
					collides = true;
					break;
				}
			}
			if(!collides){
				particles.add(p);
				total++;
			}
		}
	}
	
	private Position generateRandomPos(double r){
		double x = (Math.random() * (L-2*r)) + r;
		double y = (Math.random() * (L-2*r)) + r;
		return new Position(x,y);
	}
	
	public Cell insert(Particle p){
		int x = (int) (Math.floor(p.getPosition().getX()/(L/M)));
		int y = (int) (Math.floor(p.getPosition().getY()/(L/M)));
		cells[x][y].getParticles().add(p);
		return cells[x][y];
	}
	
	public Cell getCell(int x, int y){
		return cells[x][y];
	}
	
	public Cell getCell(Particle p){
		int x = (int) (Math.floor(p.getPosition().getX()/(L/M)));
		int y = (int) (Math.floor(p.getPosition().getY()/(L/M)));
		try{
			return cells[x][y];
		}catch (Exception e){
			System.out.println("x: " + p.getPosition().getX());
			System.out.println("y: " + p.getPosition().getY());
		}
		return null;
	}
	
	public Cell[][] getGrid(){
		return cells;
	}
	
	public int getM(){
		return M;
	}
	
	public double getL() {
		return L;
	}
	
	public Set<Particle> getParticles() {
		return particles;
	}
}
