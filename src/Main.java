import java.awt.Color;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Main {

	private static final int MAX_MASS = 15;

	public static void main(String[] args) throws InterruptedException {
		int L = 40;
		int N = 400;
		double Rc = 1;
		double noise = 0;
		
		Map<Double,Set<Particle>> map = new TreeMap<>();
		Set<Particle> set = ParticleGenerator(N,L);
		Grid grid = new CircularGrid(L, getM(L,Rc), set);
		
		Simulation s = new Simulation(grid, 500,1,Rc,noise,set);
		s.run();
		
		Input.readParticles(N, "output.txt", map);
		
		Set<Double> times = map.keySet();
		OnScreen screen = new OnScreen(L, 800, 600);
		Thread.sleep(500);
		for(Double t : times){
			Thread.sleep(200);
			Set<Particle> parts = map.get(t);
			screen.draw(parts);
			
			try {
				//screen.captureScreen();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private static Set<Particle> ParticleGenerator(int N, int L){
		double radius = 0;
		double speed = 0.3;
		Set<Particle> set = new HashSet<Particle>();
		for(int i=1 ; i<=N;i++){
			set.add(new Particle(radius, Color.RED, Math.random()*MAX_MASS, Math.random()*L, Math.random()*L, new Velocity(speed, Math.random()*(2*Math.PI))));
		}
		return set;
	}
	
	
	// ASUMIMOS QUE RMAX ES 0 PORQUE SON PUNTUALES
	private static int getM(int L, double rc){
		int max = (int)(L/rc);
		while(L%max != 0){
			max--;
		}
		return max;
	}
}
