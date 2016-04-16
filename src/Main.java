import java.awt.Color;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Generated;

public class Main {

	private static final double L = 0.5;
	private static final int N = 20;
	private static final double m = 1;
	private static final double Mbig = 100;
	private static final double r = 0.005;
	private static final double Rbig = 0.05;
	private static final double maxV = 0.1;

	public static void main(String[] args) throws InterruptedException {
		
		Particle P = new Particle(Rbig, Color.RED, Mbig, generateRandomPos(Rbig), new Velocity(0,0));
		Grid grid = new LinearGrid(L, 1);
		grid.getParticles().add(P);
		
		grid.generateParticles(maxV, r, m, N);
		
		Simulation s = new Simulation(grid, 5);
		s.run();
	}

	private static Position generateRandomPos(double r){
		double x = (Math.random() * (L-2*r)) + r;
		double y = (Math.random() * (L-2*r)) + r;
		return new Position(x,y);
	}
	
}
