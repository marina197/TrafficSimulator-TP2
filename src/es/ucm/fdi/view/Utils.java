package es.ucm.fdi.view;

import java.awt.Image;
import java.awt.Toolkit;

public class Utils {
	public static Image loadImage(String path) {
		return Toolkit.getDefaultToolkit().createImage(path);
	}
}
