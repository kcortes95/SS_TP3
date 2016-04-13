import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JFrame;
import java.util.*;

public class OnScreen extends JFrame {

	private static final long serialVersionUID = 1L;
	int width, height;
	double L;

	public OnScreen(double L, int width, int height) {
		this.width = width;
		this.height = height;
		this.L = L;
		setSize(width, height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setLocationRelativeTo(null);
	}

	public void draw(Set<Particle> particles) {

		Graphics g = this.getGraphics();
		g.clearRect(0, 0, getWidth(), getHeight());
		
		for (Particle p : particles) {
			
			setParticleColor(p);

			double radius = 2.0;
			
			double x = ((p.getPosition().getX())/L) * width;
			double y = ((p.getPosition().getY())/L) * height;
			double x2 = ((p.getV().getXVelocity())/L) * width;
			double y2 = ((p.getV().getYVelocity())/L) * height;

			Graphics2D g2 = (Graphics2D) g;
			Line2D lin = new Line2D.Double(x, y, x + x2, y + y2);
			g2.draw(lin);
			g2.setColor(p.getColor());
			Ellipse2D circle = new Ellipse2D.Double(x + x2-radius/2, y+y2-radius/2, radius, radius);
			g2.draw(circle);
		}

	}
	
	private void setParticleColor(Particle p){
		if(p.getV().getXVelocity()>0){
			if(p.getV().getYVelocity()>0)
				p.setColor(Color.BLUE);
			else
				p.setColor(Color.GREEN);
		}else{
			if(p.getV().getYVelocity()>0)
				p.setColor(Color.ORANGE);
			else
				p.setColor(Color.BLACK);
		}
	}

	/*public void saveImage() {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		this.paint(img.getGraphics());
		try {
			String nombre = "SS2-" + Calendar.getInstance().getTimeInMillis() + ".png";
			ImageIO.write(img, "png", new File(nombre));
			System.out.println("Imagen guardada como " + nombre);
		} catch (Exception e) {
			System.out.println("Imagen no guardada. " + e.getMessage());
		}
	}

	public void captureScreen() throws Exception {

		String name = "SSTP02-"+Calendar.getInstance().getTimeInMillis() + ".png";

		Rectangle screenRectangle = new Rectangle(this.getX(), this.getY()+20, this.width, this.height);
		Robot robot = new Robot();
		BufferedImage image = robot.createScreenCapture(screenRectangle);
		ImageIO.write(image, "png", new File(name));
		System.out.println("Captured screen " + name);
	}*/

}
