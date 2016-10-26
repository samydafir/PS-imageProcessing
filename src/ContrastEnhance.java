import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ContrastEnhance {
	
	public static void main(String[] args) throws IOException{
		
	float contrast = 1.5f;
	int brightness = -60;
		
	BufferedImage image = null;
	File file = new File("example.jpg");
	try {
		image = ImageIO.read(file);
	} catch (IOException e) {}
	
	RescaleOp op = new RescaleOp(contrast, brightness ,null);
	image = op.filter(image, image);
	
	ImageIO.write(image, "jpg", file);

	
	}
}