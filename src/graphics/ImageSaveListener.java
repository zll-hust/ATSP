package graphics;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageSaveListener implements ActionListener {

	private MainFrame draw;
	private String imagePath;

	public ImageSaveListener(MainFrame draw, String imagePath) {
		this.draw = draw;
		this.imagePath = imagePath;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BufferedImage bi = new BufferedImage(this.draw.getWidth(), this.draw.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

		Graphics2D graphics2d = bi.createGraphics();
		this.draw.paint(graphics2d);

		try {
			ImageIO.write(bi, "png", new File(this.imagePath + System.currentTimeMillis() + ".png"));
			System.out.println("image saved");
		} catch (IOException el) {
			System.out.println(el.getMessage());
		}

	}

}
