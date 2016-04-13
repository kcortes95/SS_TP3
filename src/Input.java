import java.awt.Color;
import java.io.File;
import java.util.*;

public class Input {

	public static void readParticles(int N, String path, Map<Double, Set<Particle>> map) {

		try {

			File file = new File(path);
			
			try {
				Scanner read = new Scanner(file);
				
				System.out.println("Reading particles from file "+path);
				while (read.hasNextLine()) {
					Double actualTime = Double.parseDouble(read.next());
					Set<Particle> parts = new TreeSet<>();
					//System.out.println("Actual time reading: "+ actualTime);
					for (int i = 0; i < N; i++) {
						int ID = Integer.parseInt(read.next()); //no lo uso para nadaaaaa
						//System.out.println("Reading data ID: " + ID);
						double x = Double.parseDouble(read.next());
						double y = Double.parseDouble(read.next());
						double angle = Double.parseDouble(read.next());
						Velocity v = new Velocity(0.3, angle);
						Particle p = new Particle(1.0, Color.BLUE , x, y, v);
						parts.add(p);
					}
					map.put(actualTime, parts);
				}
				
				read.close();

			} catch (Exception e) {
				System.out.println("Oops... Creo que tuvimos un problema");
			}

		} catch (Exception e) {
			System.out.println("Error opening or finding file");
		}
		
	}

}
