import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;


public class Output {
	private static Output instance = null;
	
	public static Output getInstace(){
		if(instance == null)
			instance = new Output();
		return instance;
	}

	public void write(Set<Particle> set, double time){
		if(time == 0){
			try{
				PrintWriter pw = new PrintWriter("output.xyz");
				pw.write((set.size()+4) + "\n");
				//comment line
				pw.write("Hola\n");
				pw.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("output.xyz", true)))) {
			out.write(25000 + "\t" + 0 + "\t" + 0 + "\t0\t0\t" + 0.005 + "\t0\t0\t0" + "\n");
			out.write(25001 + "\t" + 0 + "\t" + 0.5 + "\t0\t0\t" + 0.005 + "\t0\t0\t0" + "\n");
			out.write(25002 + "\t" + 0.5 + "\t" + 0 + "\t0\t0\t" + 0.005 + "\t0\t0\t0" + "\n");
			out.write(25004+ "\t" + 0.5 + "\t" + 0.5 + "\t0\t0\t" + 0.005 + "\t0\t0\t0" + "\n");
			for(Particle p: set){
				out.write(p.getID() + "\t" + p.getPosition().getX() + "\t" + p.getPosition().getY() + "\t" + p.getV().getXVelocity()+ "\t" + p.getV().getYVelocity() + "\t" + p.getradius() + "\t" + (p.getradius()<0.05?"255":"0") + "\t" + (p.getradius()<0.05?"255":"255") + "\t" + (p.getradius()<0.05?"255":"255")  + "\n");
			}
			out.close();
		}catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
}
